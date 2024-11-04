package com.example.cebp_project.Controller;

import com.example.cebp_project.Document;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    // List to hold the documents created
    private final List<Document> documentList = new ArrayList<>();

    @PostMapping("/create")
    public Document createDocument(@RequestParam String name, @RequestBody List<Document> dependencies) {
        Document document = new Document(name, dependencies);
        documentList.add(document);
        return document;
    }

    @GetMapping("/get")
    public Document getDocumentByName(@RequestParam String name) {
        Optional<Document> foundDocument = documentList.stream()
                .filter(doc -> doc.getName().equals(name))
                .findFirst();

        return foundDocument.orElse(null);
    }
}
