package com.example.cebp_project.Request.Customer;

public class CreateCustomerRequest {
    private int customerId;
    private String name;
    private String userName;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) { this.userName = userName; }

    public String getPass() {
        return password;
    }

    public void setPass(String pass) { this.password = pass; }
}
