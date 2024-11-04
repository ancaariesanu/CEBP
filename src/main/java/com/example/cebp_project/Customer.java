package com.example.cebp_project;

import com.example.cebp_project.*;

import java.util.HashSet;
import java.util.Set;

public class Customer extends Thread {
    private final int customerId;
    private final Set<Document> collectedDocuments = new HashSet<>();
    private final BureaucracyManager manager;

    public Customer(int customerId, BureaucracyManager manager) {
        this.customerId = customerId;
        this.manager = manager;
    }

    public int getCustomerId() {
        return customerId;
    }

    @Override
    public void run() {
        try {
            while (!manager.isDone(this)) {
                Office office = manager.getNextOffice(this);
                if (office != null) {
                    office.joinQueue(this); // Customer joins the queue
                } else {
                    System.out.println("Customer " + customerId + " has no further offices to visit.");
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void receiveDocument(Office office) {
        for (Document doc : office.getDocuments()) {
            if (doc.canBeIssued(this)) {
                collectedDocuments.add(doc);
                System.out.println("Customer " + customerId + " received document: " + doc.getName());
            }
        }
    }

    public boolean hasDocument(Document document) {
        return collectedDocuments.contains(document);
    }
}
