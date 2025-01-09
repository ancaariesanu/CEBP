package com.example.cebp_project;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BureaucracyManager {
    private final List<Office> offices;

    public BureaucracyManager(List<Office> offices) {
        this.offices = offices;
    }

    public Office getNextOffice(Customer customer) {
        for (Office office : offices) {
            for (Document doc : office.getDocuments()) {
                if (!customer.hasDocument(doc) && doc.canBeIssued(customer)) {
                    return office;
                }
            }
        }
        return null;
    }

    public boolean isDone(Customer customer) {
        for (Office office : offices) {
            for (Document doc : office.getDocuments()) {
                if (!customer.hasDocument(doc) && doc.canBeIssued(customer)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Document getDocumentById(int docId) {
        for (Office office : offices) {
            for (Document doc : office.getDocuments()) {
                if (doc.getDocumentId() == docId) { // Assuming `Document` has a `getDocumentId` method
                    return doc;
                }
            }
        }
        return null; // Return null if no document with the given `docId` is found
    }
}
