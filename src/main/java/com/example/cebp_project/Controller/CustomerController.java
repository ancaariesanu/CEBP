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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final List<Customer> customers = new ArrayList<>();
    private final BureaucracyManager bureaucracyManager;

    public CustomerController(BureaucracyManager bureaucracyManager) {
        this.bureaucracyManager = bureaucracyManager;
    }

    @PostMapping("/create")
    public String createCustomer(@RequestBody CreateCustomerRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            // Insert customer details
            String insertCustomerQuery = "INSERT INTO Customer (id, name) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertCustomerQuery)) {
                stmt.setInt(1, request.getCustomerId());
                stmt.setString(2, request.getName());
                stmt.executeUpdate();
            }
            return "Customer " + request.getCustomerId() + " created successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create customer: " + e.getMessage();
        }
    }

    @PostMapping("/setDocs")
    public String setRequiredDocuments(@RequestBody SetRequiredDocsRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            // Insert customer document requirements into Requests
            String insertRequestQuery = "INSERT INTO requests (customer_id, doc_id, status, created_at) VALUES (?, ?, ?, NOW())";
            try (PreparedStatement stmt = connection.prepareStatement(insertRequestQuery)) {
                for (int docId : request.getDocIds()) {
                    stmt.setInt(1, request.getCustomerId());
                    stmt.setInt(2, docId);
                    stmt.setString(3, "Pending");
                    stmt.executeUpdate();
                }
            }
            return "Required documents set for customer " + request.getCustomerId() + ".";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to set required documents: " + e.getMessage();
        }
    }

    @PostMapping("/createMultiple")
    public String createMultipleCustomer(@RequestBody CreateMultipleCustomerRequest request) {
        StringBuilder response = new StringBuilder("Created Customers:");
        for (Integer customerId : request.getCustomerIds()) {
            Customer customer = new Customer(customerId, bureaucracyManager);
            customers.add(customer);
            customer.start();
            response.append(" ").append(customerId);
        }
        return response.toString().trim() + " with the same document requirements";
    }

    @PostMapping("/status")
    public String checkCustomerStatus(@RequestBody CheckCustomerStatusRequest request) {
        int customerId = request.getCustomerId();
        Customer customer = findCustomerById(customerId);
        if (customer != null) {
            if (bureaucracyManager.isDone(customer)) {
                return "Customer " + customerId + " has completed all requirements.";
            } else {
                return "Customer " + customerId + " still needs documents.";
            }
        }
        return "Customer not found";
    }

    private Customer findCustomerById(int customerId) {
        return customers.stream().filter(c -> c.getCustomerId() == customerId).findFirst().orElse(null);
    }
}
