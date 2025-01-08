package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import org.springframework.web.bind.annotation.*;
import com.example.cebp_project.Counter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/counters")
public class CounterController {

    @PostMapping("/create")
    public String createCounter(@RequestBody Counter request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "INSERT INTO counter (office_id, name, doc_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getOfficeId());
                stmt.setString(2, request.getName());
                stmt.setInt(3, request.getDocId());
                stmt.executeUpdate();
            }
            return "Counter created successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error creating counter: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCounter(@PathVariable int id) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "DELETE FROM counter WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            return "Counter deleted successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting counter: " + e.getMessage();
        }
    }

    @GetMapping("/{id}")
    public Counter getCounter(@PathVariable int id) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "SELECT * FROM counter WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Counter(rs.getInt("id"), rs.getInt("office_id"), rs.getString("name"), rs.getInt("doc_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

