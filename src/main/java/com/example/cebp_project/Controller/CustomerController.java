package com.example.cebp_project.Controller;

import com.example.cebp_project.*;
import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Request.Customer.CheckCustomerStatusRequest;
import com.example.cebp_project.Request.Customer.CreateCustomerRequest;
import com.example.cebp_project.Request.Customer.CreateMultipleCustomerRequest;
import com.example.cebp_project.Request.Customer.SetRequiredDocsRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
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
            // Insert the customer and retrieve the generated ID
            String query = "INSERT INTO customer (name, user_name, pass) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, request.getName());
                stmt.setString(2, request.getUserName());
                stmt.setString(3, request.getPass());
                stmt.executeUpdate();

                // Retrieve the generated ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int customerId = generatedKeys.getInt(1);

                        // Add customer to in-memory list
                        Customer customer = new Customer(customerId, bureaucracyManager);
                        customers.add(customer);

                        return "Customer created successfully with ID: " + customerId;
                    } else {
                        return "Error: Failed to retrieve generated customer ID.";
                    }
                }
            }
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

    @GetMapping("/list")
    public List<Customer> listAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "SELECT id, name, user_name, pass FROM customer";
            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int customerId = rs.getInt("id");
                    String name = rs.getString("name");
                    String userName = rs.getString("user_name");
                    String pass = rs.getString("pass");

                    // Create customer object and add to the list
                    Customer customer = new Customer(customerId, name, userName, pass, bureaucracyManager);
                    customerList.add(customer);
                }
                // Update the in-memory list with the fetched customers
                customers.clear();  // Clear the current list before adding new data
                customers.addAll(customerList);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }


    private Customer findCustomerById(int customerId) {
        return customers.stream().filter(c -> c.getCustomerId() == customerId).findFirst().orElse(null);
    }
}
