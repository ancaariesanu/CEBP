package com.example.cebp_project.Controller;

import com.example.cebp_project.*;
import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Request.Customer.CheckCustomerStatusRequest;
import com.example.cebp_project.Request.Customer.CreateCustomerRequest;
import com.example.cebp_project.Request.Customer.CreateMultipleCustomerRequest;
import com.example.cebp_project.Request.DeleteCustomerRequest;
import com.example.cebp_project.Request.SetDocumentsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Customer API", description = "Manage customers and their document requirements.")
public class CustomerController {

    private final List<Customer> customers = new ArrayList<>();
    private final BureaucracyManager bureaucracyManager;

    public CustomerController(BureaucracyManager bureaucracyManager) {
        this.bureaucracyManager = bureaucracyManager;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new customer", description = "Creates a customer and stores their details in the database.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer created successfully."),
            @ApiResponse(responseCode = "500", description = "Failed to create customer.")
    })
    public String createCustomer(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer creation details") CreateCustomerRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "INSERT INTO customer (name, user_name, pass) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, request.getName());
                stmt.setString(2, request.getUserName());
                stmt.setString(3, request.getPass());
                stmt.executeUpdate();
            }
            return "Customer created successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create customer: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteCustomer(@RequestBody DeleteCustomerRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "DELETE FROM customer WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getId());
                stmt.executeUpdate();
            }
            return "Customer deleted successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to delete customer: " + e.getMessage();
        }
    }

    @PostMapping("/setDoc")
    @Operation(summary = "Set required documents", description = "Assigns document requirements to a customer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documents assigned successfully."),
            @ApiResponse(responseCode = "500", description = "Failed to assign documents.")
    })
    public String setRequiredDocuments(@RequestBody SetDocumentsRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "UPDATE customer SET required_docs = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setArray(1, connection.createArrayOf("INTEGER", request.getDocIds().toArray()));
                stmt.setInt(2, request.getCustomerId());
                stmt.executeUpdate();
            }
            return "Required documents updated successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to update required documents: " + e.getMessage();
        }
    }

    @PostMapping("/createMultiple")
    @Operation(summary = "Create multiple customers", description = "Creates multiple customers with the same document requirements.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customers created successfully.")
    })
    public String createMultipleCustomer(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of customer IDs") CreateMultipleCustomerRequest request) {
        StringBuilder response = new StringBuilder("Created Customers:");
        for (Integer customerId : request.getCustomerIds()) {
            Customer customer = new Customer(customerId, bureaucracyManager);
            customers.add(customer);
            customer.start();
            response.append(" ").append(customerId);
        }
        return response.toString().trim() + " with the same document requirements";
    }

    @GetMapping("/status")
    @Operation(summary = "Check customer status", description = "Checks whether a customer has completed all their document requirements.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    public String checkCustomerStatus(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer ID to check status") CheckCustomerStatusRequest request) {
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
