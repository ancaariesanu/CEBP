package com.example.cebp_project.Request.Customer;

public class CreateCustomerRequest {
    private int customerId;
    private String name;
    private String username;
    private String password;

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getUserName() {return username;}

    public String getName() {return name;}

    public String getPass() {return password;}
}
