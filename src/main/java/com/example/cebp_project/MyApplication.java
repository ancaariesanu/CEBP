package com.example.cebp_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
//        // Create documents
//        Document docA = new Document("A", Collections.emptyList());
//        Document docB = new Document("B", Arrays.asList(docA));
//        Document docC = new Document("C", Arrays.asList(docA, docB));
//
//        // Create offices
//        Office office1 = new Office(1, Arrays.asList(docA), 2);
//        Office office2 = new Office(2, Arrays.asList(docB), 1);
//        Office office3 = new Office(3, Arrays.asList(docC), 1);
//
//        // Create the bureaucracy manager
//        BureaucracyManager manager = new BureaucracyManager(Arrays.asList(office1, office2, office3));
//
//        // Start office serving
//        office1.startServing();
//        office2.startServing();
//        office3.startServing();
//
//        // Create and start customers (threads)
//        Customer customer1 = new Customer(1, manager);
//        Customer customer2 = new Customer(2, manager);
//        customer1.start();
//        customer2.start();
//
//        // Simulate a coffee break at office 1
//        try {
//            Thread.sleep(3000); // Let some customers get served
//            office1.closeCountersForCoffeeBreak();
//            Thread.sleep(2000); // Coffee break duration
//            office1.reopenAfterCoffeeBreak(); // Reopen the office after the break
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
    }
}
