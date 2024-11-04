package com.example.cebp_project.Controller;

import com.example.cebp_project.BureaucracyManager;
import com.example.cebp_project.Document;
import com.example.cebp_project.Office;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/office")
public class OfficeController {

    private final BureaucracyManager bureaucracyManager;
    private final List<Office> offices;

    public OfficeController() {
        offices = new CopyOnWriteArrayList<>();
        bureaucracyManager = new BureaucracyManager(offices);
    }

    @PostMapping("/create")
    public Office createOffice(@RequestParam int officeId, @RequestParam int numberOfCounters, @RequestBody List<Document> documents) {
        Office office = new Office(officeId, documents, numberOfCounters);
        offices.add(office);
        System.out.println(office);
        return office;
    }

    @PostMapping("/{officeId}/start")
    public String startServing(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            office.startServing();
            return "Started serving at office " + officeId;
        }
        return "Office not found";
    }

    @PostMapping("/{officeId}/closeForBreak")
    public String closeForBreak(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            office.closeCountersForCoffeeBreak();
            return "Office " + officeId + " is on a coffee break.";
        }
        return "Office not found";
    }

    @PostMapping("/{officeId}/reopen")
    public String reopenAfterBreak(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            office.reopenAfterCoffeeBreak();
            return "Office " + officeId + " has reopened after coffee break.";
        }
        return "Office not found";
    }
    @GetMapping("/{officeId}/status")
    public String checkOfficeStatus(@PathVariable int officeId) {
        Office office = findOfficeById(officeId);
        if (office != null) {
            if (office.getOfficeStatus()) {
                return "Office " + officeId + " is currently on a coffee break.";
            } else {
                return "Office " + officeId + " is currently open.";
            }
        }
        return "Office not found";
    }

    private Office findOfficeById(int officeId) {
        return offices.stream().filter(o -> o.getOfficeId() == officeId).findFirst().orElse(null);
    }
}
