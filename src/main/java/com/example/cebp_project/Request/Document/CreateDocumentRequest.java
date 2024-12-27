package com.example.cebp_project.Request.Document;

import java.util.List;

public class CreateDocumentRequest {
    private String name;
    private List<String> dependencies;
    private String description;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}
}
