package com.example.cebp_project.Controller;

import com.example.cebp_project.BureaucracyManager;
import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Office;
import com.example.cebp_project.Request.Office.CreateOfficeRequest;
import com.example.cebp_project.Request.Office.OfficeIdRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    private final BureaucracyManager bureaucracyManager;
    private final List<Office> offices;

    public OfficeController() {
        offices = new CopyOnWriteArrayList<>();
        bureaucracyManager = new BureaucracyManager(offices);
    }

    @PostMapping("/create")
    public String createOffice(@RequestBody CreateOfficeRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String insertOfficeQuery = "INSERT INTO offices (id, number_of_counters) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertOfficeQuery)) {
                stmt.setInt(1, request.getOfficeId());
                stmt.setInt(2, request.getNumberOfCounters());
                stmt.executeUpdate();
            }

            String insertDocumentsQuery = "INSERT INTO office_documents (office_id, document_name) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertDocumentsQuery)) {
                for (String docName : request.getDocumentNames()) {
                    stmt.setInt(1, request.getOfficeId());
                    stmt.setString(2, docName);
                    stmt.executeUpdate();
                }
            }

            return "Office " + request.getOfficeId() + " created.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create office: " + e.getMessage();
        }
    }

    @PostMapping("/startServing")
    public String startServing(@RequestBody OfficeIdRequest request) {
        Office office = findOfficeById(request.getOfficeId());
        if (office != null) {
            office.startServing();
            return "Started serving at office " + request.getOfficeId();
        }
        return "Office not found";
    }

    @PostMapping("/closeForBreak")
    public String closeForBreak(@RequestBody OfficeIdRequest request) {
        Office office = findOfficeById(request.getOfficeId());
        if (office != null) {
            office.closeCountersForCoffeeBreak();
            return "Office " + request.getOfficeId() + " is on a coffee break.";
        }
        return "Office not found";
    }

    @PostMapping("/reopen")
    public String reopenAfterBreak(@RequestBody OfficeIdRequest request) {
        Office office = findOfficeById(request.getOfficeId());
        if (office != null) {
            office.reopenAfterCoffeeBreak();
            return "Office " + request.getOfficeId() + " has reopened after coffee break.";
        }
        return "Office not found";
    }

    @GetMapping("/{officeId}/status")
    public String checkOfficeStatus(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            if (office.getOfficeStatus()) {
                return "Office " + officeId + " is currently on a coffee break.";
            } else {
                return "Office " + officeId + " is currently open.";
            }
        }
        return "Office not found";
    }

    private Office findOfficeById(int officeId) {
        return offices.stream().filter(o -> o.getOfficeId() == officeId).findFirst().orElse(null);
    }
}
