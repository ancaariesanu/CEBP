package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Document;
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

    // List to hold the documents created
    private final List<Document> documentList = new ArrayList<>();

    @PostMapping("/create")
    public String createDocument(@RequestParam String name, @RequestParam List<String> dependencies) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String insertDocQuery = "INSERT INTO documents (name) VALUES (?) RETURNING id";
            int documentId;

            // Insert document
            try (PreparedStatement stmt = connection.prepareStatement(insertDocQuery)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                documentId = rs.getInt("id");
            }

            // Insert dependencies
            String insertDependencyQuery = "INSERT INTO document_dependencies (document_id, dependency_name) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertDependencyQuery)) {
                for (String dependency : dependencies) {
                    stmt.setInt(1, documentId);
                    stmt.setString(2, dependency);
                    stmt.executeUpdate();
                }
            }

            return "Document " + name + " created with dependencies.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create document: " + e.getMessage();
        }
    }

    @GetMapping("/get")
    public Document getDocumentByName(@RequestParam String name) {
        Optional<Document> foundDocument = documentList.stream()
                .filter(doc -> doc.getName().equals(name))
                .findFirst();

        return foundDocument.orElse(null);
    }
}
