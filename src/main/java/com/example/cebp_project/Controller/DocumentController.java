package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Document;
import com.example.cebp_project.Request.Document.CreateDocumentRequest;
import com.example.cebp_project.Request.Document.GetDocumentRequest;
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
            String insertDocQuery = "INSERT INTO documents (name) VALUES (?) RETURNING id";
            int documentId;

            // Insert the main document
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

            // Insert dependencies
            if (!request.getDependencies().isEmpty()) {
                String getDependencyIdQuery = "SELECT id FROM documents WHERE name = ?";
                String insertDependencyQuery = "INSERT INTO doc_dep_inter (doc_id, dep_id) VALUES (?, ?)";

                try (PreparedStatement selectStmt = connection.prepareStatement(getDependencyIdQuery);
                     PreparedStatement insertStmt = connection.prepareStatement(insertDependencyQuery)) {

                    for (String dependencyName : request.getDependencies()) {
                        // Retrieve dependency ID
                        selectStmt.setString(1, dependencyName);
                        try (ResultSet rs = selectStmt.executeQuery()) {
                            if (rs.next()) {
                                int dependencyId = rs.getInt("id");

                                // Insert into doc_dep_inter table
                                insertStmt.setInt(1, documentId);
                                insertStmt.setInt(2, dependencyId);
                                insertStmt.executeUpdate();
                            } else {
                                System.err.println("Dependency not found: " + dependencyName);
                            }
                        }
                    }
                }
            }

            return "Document " + request.getName() + " created with dependencies.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create document: " + e.getMessage();
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

    private List<Document> getDependenciesForDocument(int documentId, Connection connection) {
        List<Document> dependencies = new ArrayList<>();
        String dependencyQuery = "SELECT d.id, d.name, d.description " +
                "FROM doc_dep_inter dd " +
                "JOIN documents d ON dd.dependency_id = d.id " +
                "WHERE dd.document_id = ?";
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
