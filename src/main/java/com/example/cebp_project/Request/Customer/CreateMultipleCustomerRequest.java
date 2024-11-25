package com.example.cebp_project.Request.Customer;

import java.util.List;

public class CreateMultipleCustomerRequest {
    private List<Integer> customerIds;

    public List<Integer> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Integer> customerIds) {
        this.customerIds = customerIds;
    }
}
