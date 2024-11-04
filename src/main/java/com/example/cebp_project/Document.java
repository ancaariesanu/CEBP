package com.example.cebp_project;

import com.example.cebp_project.*;

import java.util.List;

public class Document {
    private final String name;
    private final List<Document> dependencies;

    public Document(String name, List<Document> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
    }

    public String getName() {
        return name;
    }

    public List<Document> getDependencies() {
        return dependencies;
    }

    public boolean canBeIssued(Customer customer) {
        for (Document dependency : dependencies) {
            if (!customer.hasDocument(dependency)) {
                return false;
            }
        }
        return true;
    }
}
