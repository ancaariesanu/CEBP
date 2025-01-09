package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import org.springframework.web.bind.annotation.*;
import com.example.cebp_project.Counter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/counters")
public class CounterController {
    private final List<Counter> localCounters = new ArrayList<>();

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

    @GetMapping("/list")
    public List<Counter> listAllCounters() {
        List<Counter> counters = new ArrayList<>();
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "SELECT * FROM counter";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    // Create and add Counter to list
                    Counter counter = new Counter(rs.getInt("id"), rs.getInt("office_id"), rs.getString("name"), rs.getInt("doc_id"));
                    counters.add(counter);

                    // Save it locally as well
                    localCounters.add(counter);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counters;
    }

    // Optionally, you can return the locally saved counters with another endpoint
    @GetMapping("/local")
    public List<Counter> getLocalCounters() {
        return localCounters;
    }
}

