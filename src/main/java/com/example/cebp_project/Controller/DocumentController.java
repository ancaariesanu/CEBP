package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Document;
import com.example.cebp_project.Request.Document.CreateDocumentRequest;
import com.example.cebp_project.Request.Document.GetDocumentRequest;
import com.example.cebp_project.Request.Document.SetDescriptionRequest;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final List<Document> documentList = new ArrayList<>();

    @PostMapping("/create")
    public String createDocument(@RequestBody CreateDocumentRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            connection.setAutoCommit(false); // Enable transaction management
            int documentId;

            // Insert the main document
            String insertDocQuery = "INSERT INTO documents (name) VALUES (?) RETURNING id";
            try (PreparedStatement stmt = connection.prepareStatement(insertDocQuery)) {
                stmt.setString(1, request.getName());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        documentId = rs.getInt("id");
                    } else {
                        throw new SQLException("Failed to retrieve document ID after insertion.");
                    }
                }
            }

            // Handle dependencies
            if (!request.getDependencies().isEmpty()) {
                String getDependencyIdQuery = "SELECT id FROM documents WHERE name = ?";
                String insertNewDependencyQuery = "INSERT INTO documents (name) VALUES (?) RETURNING id";
                String insertDependencyQuery = "INSERT INTO doc_dep_inter (doc_id, dep_id) VALUES (?, ?)";

                try (PreparedStatement selectStmt = connection.prepareStatement(getDependencyIdQuery);
                     PreparedStatement insertNewDepStmt = connection.prepareStatement(insertNewDependencyQuery);
                     PreparedStatement insertDepStmt = connection.prepareStatement(insertDependencyQuery)) {

                    for (String dependencyName : request.getDependencies()) {
                        int dependencyId;

                        // Check if dependency exists
                        selectStmt.setString(1, dependencyName);
                        try (ResultSet rs = selectStmt.executeQuery()) {
                            if (rs.next()) {
                                dependencyId = rs.getInt("id"); // Dependency exists, retrieve its ID
                            } else {
                                // Dependency does not exist, create it
                                insertNewDepStmt.setString(1, dependencyName);
                                try (ResultSet newDepRs = insertNewDepStmt.executeQuery()) {
                                    if (newDepRs.next()) {
                                        dependencyId = newDepRs.getInt("id");
                                    } else {
                                        throw new SQLException("Failed to insert new dependency: " + dependencyName);
                                    }
                                }
                            }
                        }

                        // Insert into doc_dep_inter table
                        insertDepStmt.setInt(1, documentId);
                        insertDepStmt.setInt(2, dependencyId);
                        insertDepStmt.executeUpdate();
                    }
                }
            }

            connection.commit(); // Commit the transaction
            return "Document '" + request.getName() + "' created with dependencies.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create document: " + e.getMessage();
        }
    }

    @PostMapping("/setDescription")
    public String setDocumentDescription(@RequestBody SetDescriptionRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String updateDescriptionQuery = "UPDATE documents SET description = ? WHERE id = ?";

            try (PreparedStatement stmt = connection.prepareStatement(updateDescriptionQuery)) {
                stmt.setString(1, request.getDescription());
                stmt.setInt(2, request.getDocumentId());
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    return "Description for document " + request.getDocumentId() + " updated successfully.";
                } else {
                    return "Document not found.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to update description: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDocument(@PathVariable int id) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "DELETE FROM documents WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            return "Document deleted successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting document: " + e.getMessage();
        }
    }

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable int id) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            // Fetch the main document
            String query = "SELECT id, name, description FROM documents WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Create the document object
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    // Retrieve dependencies (optional)
                    List<Document> dependencies = getDependenciesForDocument(id, connection);

                    return new Document(name, dependencies, description);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/list")
    public List<Document> listAllDocuments() {
        List<Document> documents = new ArrayList<>();
        try (Connection connection = SupabaseConfig.getConnection()) {
            String selectQuery = "SELECT id, name, description FROM documents";
            try (PreparedStatement stmt = connection.prepareStatement(selectQuery);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int documentId = rs.getInt("id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    // Retrieve dependencies for each document
                    List<Document> dependencies = getDependenciesForDocument(documentId, connection);

                    // Add the document to the list with dependencies
                    documents.add(new Document(name, dependencies, description));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }



    private List<Document> getDependenciesForDocument(int documentId, Connection connection) {
        List<Document> dependencies = new ArrayList<>();
        String dependencyQuery = "SELECT d.id, d.name, d.description " +
                "FROM doc_dep_inter dd " +
                "JOIN documents d ON dd.dep_id = d.id " +
                "WHERE dd.doc_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(dependencyQuery)) {
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String dependencyName = rs.getString("name");
                    String dependencyDescription = rs.getString("description");

                    // Pass an empty list for dependencies of dependencies (recursive retrieval can be implemented if needed)
                    dependencies.add(new Document(dependencyName, List.of(), dependencyDescription));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dependencies;
    }


}
