package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Counter;
import com.example.cebp_project.Office;
import com.example.cebp_project.Customer;
import com.example.cebp_project.Document;
import com.example.cebp_project.Request.Office.CreateOfficeRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/offices")
public class OfficeController {
    private final List<Office> offices = new CopyOnWriteArrayList<>();

    @PostMapping("/create")
    public String createOffice(@RequestBody CreateOfficeRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            // Insert the office into the database and retrieve the generated ID
            String insertOfficeQuery = "INSERT INTO office (name, counter_no, is_closed) VALUES (?, ?, ?) RETURNING id";
            int generatedId;

            try (PreparedStatement stmt = connection.prepareStatement(insertOfficeQuery)) {
                stmt.setString(1, request.getName());
                stmt.setInt(2, request.getCounterNo());
                stmt.setBoolean(3, request.isClosed());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        generatedId = rs.getInt("id");
                    } else {
                        throw new SQLException("Failed to retrieve generated ID.");
                    }
                }
            }

            // Create the Office object with the generated ID and save it locally
            Office createdOffice = new Office(generatedId, request.getName(), request.getCounterNo());
            offices.add(createdOffice);

            return "Office " + generatedId + " created successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create office: " + e.getMessage();
        }
    }



    @PostMapping("/{officeId}/addDocument")
    public String addDocumentToOffice(@PathVariable int officeId, @RequestBody Document document) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            office.addDocument(document);
            return "Document " + document.getDocumentId() + " added to Office " + officeId;
        }
        return "Office not found.";
    }

    @PostMapping("/{officeId}/addCounter/{counterId}")
    public String addCounterToOffice(@PathVariable int officeId, @PathVariable int counterId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            // Find the counter by counterId (assuming you have a method to get Counter by id)
            Counter counter = findCounterById(counterId);

            if (counter != null) {
                // Set the officeId for the counter if not already set
                if (counter.getOfficeId() == 0) {
                    counter.setOfficeId(officeId); // Assign officeId to counter if not set
                }

                // Add the counter to the office
                boolean added = office.addCounter(counter);
                return added ? "Counter " + counter.getName() + " added to Office " + officeId
                        : "Unable to add more counters to Office " + officeId;
            } else {
                return "Counter with ID " + counterId + " not found.";
            }
        }
        return "Office not found.";
    }

    private Counter findCounterById(int counterId) {
        // Iterate through the list of offices
        for (Office office : offices) {
            // Iterate through the list of counters in each office
            for (Counter counter : office.getCounters()) {
                // Check if the counter ID matches
                if (counter.getId() == counterId) {
                    return counter; // Return the counter if found
                }
            }
        }
        return null; // Return null if no matching counter is found
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
        System.out.println(office);
        if (office != null) {
            return office.isClosed() ? "Office " + officeId + " is on a coffee break." : "Office " + officeId + " is open.";
        }
        return "Office not found.";
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<Office> listAllOffices() {
        List<Office> result = new ArrayList<>();
        try (Connection connection = SupabaseConfig.getConnection()) {
            String selectQuery = "SELECT id, name, counter_no, is_closed FROM office";
            System.out.println("Fetching offices from database...");
            try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    // Add each office to the result list
                    result.add(new Office(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("counter_no")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
