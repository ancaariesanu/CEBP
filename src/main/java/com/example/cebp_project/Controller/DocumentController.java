package com.example.cebp_project.Controller;

import com.example.cebp_project.Config.SupabaseConfig;
import com.example.cebp_project.Document;
import com.example.cebp_project.Request.DeleteDocumentRequest;
import com.example.cebp_project.Request.Document.CreateDocumentRequest;
import com.example.cebp_project.Request.Document.GetDocumentRequest;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.parameters.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
            String insertDocQuery = "INSERT INTO documents (name, description) VALUES (?, ?) RETURNING id";
            int documentId;

            try (PreparedStatement stmt = connection.prepareStatement(insertDocQuery)) {
                stmt.setString(1, request.getName());
                stmt.setString(2, request.getDescription());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                documentId = rs.getInt("id");
            }

            String insertDependencyQuery = "INSERT INTO doc_dep_inter (doc_id, dep_id) VALUES (?, ?)";
//            try (PreparedStatement stmt = connection.prepareStatement(insertDependencyQuery)) {
//                for (Integer dependencyId : request.getDependencies()) {
//                    stmt.setInt(1, documentId);
//                    stmt.setInt(2, dependencyId);
//                    stmt.executeUpdate();
//                }
//            }
            return "Document created successfully with dependencies.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to create document: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public String deleteDocument(@RequestBody DeleteDocumentRequest request) {
        try (Connection connection = SupabaseConfig.getConnection()) {
            String query = "DELETE FROM documents WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, request.getId());
                stmt.executeUpdate();
            }
            return "Document deleted successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to delete document: " + e.getMessage();
        }
    }

//    @PostMapping("/get")
//    public Document getDocument(@RequestBody GetDocumentRequest request) {
//        try (Connection connection = SupabaseConfig.getConnection()) {
//            String query = "SELECT * FROM documents WHERE id = ?";
//            try (PreparedStatement stmt = connection.prepareStatement(query)) {
//                stmt.setInt(1, request.getId());
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    Document doc = new Document(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
//
//                    // Fetch dependencies
//                    String depQuery = "SELECT dep_id FROM doc_dep_inter WHERE doc_id = ?";
//                    try (PreparedStatement depStmt = connection.prepareStatement(depQuery)) {
//                        depStmt.setInt(1, doc.getId());
//                        ResultSet depRs = depStmt.executeQuery();
//                        while (depRs.next()) {
//                            doc.getDependencies().add(depRs.getInt("dep_id"));
//                        }
//                    }
//                    return doc;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
