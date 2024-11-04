package com.example.cebp_project.Controller;

import com.example.cebp_project.*;
import org.springframework.web.bind.annotation.*;

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
    public String createCustomer(@RequestParam int customerId) {
        Customer customer = new Customer(customerId, bureaucracyManager);
        customers.add(customer);
        customer.start();
        return "Customer " + customerId + " created and started.";
    }

    @GetMapping("/{customerId}/status")
    public String checkCustomerStatus(@PathVariable int customerId) {
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
