package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Office;
import com.example.cebp_project.Customer;
import com.example.cebp_project.Document;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    private final List<Office> offices = new CopyOnWriteArrayList<>();

    @PostMapping("/create")
    public String createOffice(@RequestBody Office office) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String insertOfficeQuery = "INSERT INTO office (id, name, counter_no, is_closed) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertOfficeQuery)) {
                stmt.setInt(1, office.getId());
                stmt.setString(2, office.getName());
                stmt.setInt(3, office.getCounterNo());
                stmt.setBoolean(4, office.isClosed());
                stmt.executeUpdate();
            }
            // Add to in-memory list
            offices.add(new Office(office.getId(), office.getName(), office.getDocuments(), office.getCounterNo()));
            return "Office " + office.getId() + " created successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create office: " + e.getMessage();
        }
    }

    @PostMapping("/{officeId}/joinQueue")
    public String joinQueue(@PathVariable int officeId, @RequestBody Customer customer) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            try {
                office.joinQueue(customer);
                return "Customer " + customer.getCustomerId() + " joined the queue at Office " + officeId;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Failed to add customer to the queue.";
            }
        }
        return "Office not found.";
    }

    @PostMapping("/{officeId}/startServing")
    public String startServing(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            office.startServing();
            return "Started serving at office " + officeId;
        }
        return "Office not found.";
    }

    @PostMapping("/{officeId}/closeForBreak")
    public String closeForBreak(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            office.closeCountersForCoffeeBreak();
            updateOfficeStatusInDB(officeId, true); // Update database
            return "Office " + officeId + " is closed for a coffee break.";
        }
        return "Office not found.";
    }

    @PostMapping("/{officeId}/reopen")
    public String reopenAfterBreak(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            office.reopenAfterCoffeeBreak();
            updateOfficeStatusInDB(officeId, false); // Update database
            return "Office " + officeId + " has reopened after a coffee break.";
        }
        return "Office not found.";
    }

    @GetMapping("/{officeId}/documents")
    public List<Document> getDocuments(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            return office.getDocuments();
        }
        return new ArrayList<>();
    }

    @GetMapping("/{officeId}/status")
    public String getOfficeStatus(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            return office.isClosed() ? "Office " + officeId + " is on a coffee break." : "Office " + officeId + " is open.";
        }
        return "Office not found.";
    }

    @GetMapping("/list")
    public List<Office> listAllOffices() {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String selectQuery = "SELECT id, name, counter_no, is_closed FROM office";
            try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    List<Office> offices = new ArrayList<>();
                    List<Document> depedencies = new ArrayList<>();
                    offices.add(new Office(rs.getInt("id"), rs.getString("name"), depedencies, rs.getInt("counter_no")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offices;
    }

    private Office findOfficeById(int officeId) {
        return offices.stream().filter(o -> o.getId() == officeId).findFirst().orElse(null);
    }

    private void updateOfficeStatusInDB(int officeId, boolean isClosed) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String updateOfficeQuery = "UPDATE office SET is_closed = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateOfficeQuery)) {
                stmt.setBoolean(1, isClosed);
                stmt.setInt(2, officeId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
