package com.example.cebp_project;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

public class Office {
    private final int officeId;
    private String officeName = "";
    private final CopyOnWriteArrayList<Document> documents = new CopyOnWriteArrayList<>();
    private final BlockingQueue<Customer> queue;
    private final ReentrantLock countersLock = new ReentrantLock();
    private final ExecutorService counterExecutor;
    private volatile boolean isCoffeeBreak;
    private final int numberOfCounters;
    private final List<Counter> counters = new ArrayList<>(); // List to store counters

    public Office(int officeId, String officeName, int numberOfCounters) {
        this.officeId = officeId;
        this.officeName = officeName;
        this.queue = new LinkedBlockingQueue<>();
        this.numberOfCounters = numberOfCounters;
        this.counterExecutor = Executors.newFixedThreadPool(numberOfCounters);
        this.isCoffeeBreak = false;
    }

    // Method to add a counter to the office
    public boolean addCounter(Counter counter) {
        if (counters.size() < numberOfCounters) {
            counters.add(counter);
            System.out.println("Added counter " + counter.getName() + " to Office " + officeId);
            return true;
        } else {
            System.out.println("Cannot add more counters to Office " + officeId + ". Maximum counters reached.");
            return false; // Cannot add more counters than the allowed number
        }
    }

    // Method to join the queue
    public void joinQueue(Customer customer) throws InterruptedException {
        queue.put(customer);
        System.out.println("Customer " + customer.getCustomerId() + " joined the queue at Office " + officeId);
    }

    // Method to start serving customers
    public void startServing() {
        for (int i = 0; i < ((ThreadPoolExecutor) counterExecutor).getCorePoolSize(); i++) {
            counterExecutor.submit(() -> {
                while (!isCoffeeBreak) {
                    try {
                        countersLock.lock();
                        Customer customer = queue.take();
                        serveCustomer(customer);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        countersLock.unlock();
                    }
                }
            });
        }
    }

    // Method to serve a customer
    private void serveCustomer(Customer customer) {
        System.out.println("Serving customer " + customer.getCustomerId() + " at Office " + officeId);
        customer.receiveDocument(this);
    }

    // Method to close office for coffee break
    public void closeCountersForCoffeeBreak() {
        isCoffeeBreak = true;
        System.out.println("Office " + officeId + " is closed for a coffee break.");
    }

    // Method to reopen office after coffee break
    public void reopenAfterCoffeeBreak() {
        isCoffeeBreak = false;
        System.out.println("Office " + officeId + " has reopened after the coffee break.");
        startServing();
    }

    // Method to add a document to the office
    public void addDocument(Document document) {
        documents.add(document);
        System.out.println("Added document " + document.getDocumentId() + " to Office " + officeId);
    }

    // Getter for the list of documents
    public List<Document> getDocuments() {
        return documents;
    }

    // Getter for the office ID
    public int getId() {
        return officeId;
    }

    // Getter for the office name
    public String getName() {
        return officeName;
    }

    // Getter for the number of counters
    public int getCounterNo() {
        return numberOfCounters;
    }

    // Getter for the list of counters
    public List<Counter> getCounters() {
        return counters;
    }

    // Check if the office is closed for a coffee break
    public boolean isClosed() {
        return isCoffeeBreak;
    }
}
