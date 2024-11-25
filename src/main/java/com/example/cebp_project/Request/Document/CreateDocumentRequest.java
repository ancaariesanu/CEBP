package com.example.cebp_project.Request.Document;

import java.util.List;

public class CreateDocumentRequest {
    private String name;
    private List<String> dependencies;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }
}
