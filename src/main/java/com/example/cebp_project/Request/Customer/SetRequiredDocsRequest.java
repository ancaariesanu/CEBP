package com.example.cebp_project.Request.Customer;

import java.util.List;

public class SetRequiredDocsRequest {
    private int customerId;
    private List<Integer> docIds;

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(List<Integer> docIds) {
        this.docIds = docIds;
    }
}
