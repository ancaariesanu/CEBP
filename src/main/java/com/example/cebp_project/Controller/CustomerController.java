package com.example.cebp_project.Controller;

import com.example.cebp_project.*;
import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Request.Customer.CheckCustomerStatusRequest;
import com.example.cebp_project.Request.Customer.CreateCustomerRequest;
import com.example.cebp_project.Request.Customer.CreateMultipleCustomerRequest;
import com.example.cebp_project.Request.Customer.SetRequiredDocsRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final List<Customer> customers = new ArrayList<>();
    private final BureaucracyManager bureaucracyManager;

    public CustomerController(BureaucracyManager bureaucracyManager) {
        this.bureaucracyManager = bureaucracyManager;
    }

    @PostMapping("/create")
    public String createCustomer(@RequestBody CreateCustomerRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "INSERT INTO customer (id, name, user_name, pass) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getCustomerId());
                stmt.setString(2, request.getName());
                stmt.setString(3, request.getUserName());
                stmt.setString(4, request.getPass());
                stmt.executeUpdate();
            }
            // Add customer to in-memory list
            Customer customer = new Customer(request.getCustomerId(), bureaucracyManager);
            customers.add(customer);
            return "Customer created successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error creating customer: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable int id) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "DELETE FROM customer WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            // Remove from in-memory list
            customers.removeIf(c -> c.getCustomerId() == id);
            return "Customer deleted successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting customer: " + e.getMessage();
        }
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable int id) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "SELECT * FROM customer WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("user_name"), rs.getString("pass"), bureaucracyManager);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/setDocs")
    public String setRequiredDocuments(@RequestBody SetRequiredDocsRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            // Update database with required documents
            String insertRequestQuery = "INSERT INTO requests (customer_id, doc_id, status, created_at) VALUES (?, ?, ?, NOW())";
            try (PreparedStatement stmt = connection.prepareStatement(insertRequestQuery)) {
                for (int docId : request.getDocIds()) {
                    stmt.setInt(1, request.getCustomerId());
                    stmt.setInt(2, docId);
                    stmt.setString(3, "Pending");
                    stmt.executeUpdate();
                }
            }

            // Update in-memory customer
            Customer customer = findCustomerById(request.getCustomerId());
            if (customer != null) {
                for (int docId : request.getDocIds()) {
                    Document document = bureaucracyManager.getDocumentById(docId);
                    customer.addRequiredDocument(document);
                }
            }
            return "Required documents set for customer " + request.getCustomerId() + ".";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to set required documents: " + e.getMessage();
        }
    }

    @PostMapping("/status")
    public String checkCustomerStatus(@RequestBody CheckCustomerStatusRequest request) {
        Customer customer = findCustomerById(request.getCustomerId());
        if (customer != null) {
            if (bureaucracyManager.isDone(customer)) {
                return "Customer " + request.getCustomerId() + " has completed all requirements.";
            } else {
                return "Customer " + request.getCustomerId() + " still needs documents.";
            }
        }
        return "Customer not found.";
    }

    private Customer findCustomerById(int customerId) {
        return customers.stream().filter(c -> c.getCustomerId() == customerId).findFirst().orElse(null);
    }
}
