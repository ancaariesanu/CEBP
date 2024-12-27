package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Request.CreateCounterRequest;
import com.example.cebp_project.Request.DeleteCounterRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/counter")
public class CounterController {

    @PostMapping("/create")
    public String createCounter(@RequestBody CreateCounterRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "INSERT INTO counter (office_id, name, doc_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getOfficeId());
                stmt.setString(2, request.getName());
                stmt.setInt(3, request.getDocId());
                stmt.executeUpdate();
            }
            return "Counter created successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create counter: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteCounter(@RequestBody DeleteCounterRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "DELETE FROM counter WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getId());
                stmt.executeUpdate();
            }
            return "Counter deleted successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to delete counter: " + e.getMessage();
        }
    }
}

