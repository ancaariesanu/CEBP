package com.example.cebp_project;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class Customer extends Thread {
    private final int customerId;
    private final Set<Document> collectedDocuments = new HashSet<>();
    private final BureaucracyManager manager;
    private final ReentrantLock documentLock = new ReentrantLock();

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
        documentLock.lock();
        try {
            for (Document doc : office.getDocuments()) {
                if (doc.canBeIssued(this)) {
                    collectedDocuments.add(doc);
                    System.out.println("Customer " + customerId + " received document: " + doc.getName());
                }
            }
        } finally {
            documentLock.unlock();
        }
    }

    public boolean hasDocument(Document document) {
        documentLock.lock();
        try {
            return collectedDocuments.contains(document);
        } finally {
            documentLock.unlock();
        }
    }
}