package com.example.cebp_project.Request.Document;

public class SetDescriptionRequest {
    private int documentId;
    private String description;

    // Getters and setters
    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

