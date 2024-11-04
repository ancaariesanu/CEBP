package com.example.cebp_project.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

    @PostMapping("/resource")
    public String handlePostRequest(@RequestBody String payload) {
        // Your logic for handling the POST request
        System.out.println("Received payload: " + payload);
        return "Request processed";
    }
}
