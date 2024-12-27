package com.example.cebp_project.Controller;

import com.example.cebp_project.BureaucracyManager;
import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Office;
import com.example.cebp_project.Request.DeleteOfficeRequest;
import com.example.cebp_project.Request.Office.CreateOfficeRequest;
import com.example.cebp_project.Request.Office.OfficeIdRequest;
import com.example.cebp_project.Request.OfficeStatusRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    private final List<Office> offices;

    public OfficeController() {
        offices = new CopyOnWriteArrayList<>();
        BureaucracyManager bureaucracyManager = new BureaucracyManager(offices);
    }

    @PostMapping("/create")
    public String createOffice(@RequestBody CreateOfficeRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "INSERT INTO office (name, counter_no) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, request.getName());
                stmt.setInt(2, request.getNumberOfCounters());
                stmt.executeUpdate();
            }
            return "Office created successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create office: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteOffice(@RequestBody DeleteOfficeRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "DELETE FROM office WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getId());
                stmt.executeUpdate();
            }
            return "Office deleted successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to delete office: " + e.getMessage();
        }
    }

    @PostMapping("/close")
    public String closeOffice(@RequestBody OfficeStatusRequest request) {
        return updateOfficeStatus(request.getId(), true);
    }

    @PostMapping("/open")
    public String openOffice(@RequestBody OfficeStatusRequest request) {
        return updateOfficeStatus(request.getId(), false);
    }

    @PostMapping("/status")
    public boolean getOfficeStatus(@RequestBody OfficeStatusRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "SELECT is_closed FROM office WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getBoolean("is_closed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String updateOfficeStatus(int id, boolean status) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "UPDATE office SET is_closed = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setBoolean(1, status);
                stmt.setInt(2, id);
                stmt.executeUpdate();
            }
            return "Office status updated successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to update office status: " + e.getMessage();
        }
    }
}
