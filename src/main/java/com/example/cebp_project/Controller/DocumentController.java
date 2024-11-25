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

            // Insert document
            try (PreparedStatement stmt = connection.prepareStatement(insertDocQuery)) {
                stmt.setString(1, request.getName());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                documentId = rs.getInt("id");
            }

            // Insert dependencies
            String insertDependencyQuery = "INSERT INTO document_dependencies (document_id, dependency_name) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertDependencyQuery)) {
                for (String dependency : request.getDependencies()) {
                    stmt.setInt(1, documentId);
                    stmt.setString(2, dependency);
                    stmt.executeUpdate();
                }
            }

            return "Document " + request.getName() + " created with dependencies.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create document: " + e.getMessage();
        }
    }

    // Endpoint to get a document by its name
    @PostMapping("/get")
    public Document getDocumentByName(@RequestBody GetDocumentRequest request) {
        Optional<Document> foundDocument = documentList.stream()
                .filter(doc -> doc.getName().equals(request.getName()))
                .findFirst();

        return foundDocument.orElse(null);
    }
}
