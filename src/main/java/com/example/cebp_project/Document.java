package com.example.cebp_project;

import com.example.cebp_project.Config.SupabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Document {
    private final String name;
    private int documentId; // Made non-final to allow setting it from the database
    private final List<Document> dependencies;
    private final String description;

    public Document(String name, List<Document> dependencies, String description) {
        this.name = name;
        this.dependencies = dependencies;
        this.documentId = getDocumentIdFromDb();
        this.description = description; // Retrieve document ID during initialization
    }

    public int getDocumentIdFromDb() {
        String query = "SELECT id FROM documents WHERE name = ?";
        try (Connection con = SupabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, this.name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the document ID could not be found
    }

    public void insertDependencies() {
        String insertDependencyQuery = "INSERT INTO doc_dep_inter (doc_id, dep_id) VALUES (?, ?)";
        String getDependencyIdQuery = "SELECT id FROM documents WHERE name = ?";

        try (Connection connection = SupabaseConfig.getConnection()) {
            try (PreparedStatement insertStmt = connection.prepareStatement(insertDependencyQuery);
                 PreparedStatement selectStmt = connection.prepareStatement(getDependencyIdQuery)) {

                for (Document dependency : dependencies) {
                    // Get the ID of the dependency
                    selectStmt.setString(1, dependency.getName());
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            int dependencyId = rs.getInt("id");

                            // Insert the doc_id and dep_id into the table
                            insertStmt.setInt(1, this.documentId);
                            insertStmt.setInt(2, dependencyId);
                            insertStmt.executeUpdate();
                        } else {
                            System.err.println("Dependency not found in the database: " + dependency.getName());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public int getDocumentId() {
        return documentId;
    }

    public String getDescription() {
        return description;
    }
}
