package com.example.cebp_project.Request.Office;

import java.util.List;

public class CreateOfficeRequest {
    private int officeId;
    private int numberOfCounters;
    private String officeName;

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getNumberOfCounters() {
        return numberOfCounters;
    }

    public void setNumberOfCounters(int numberOfCounters) {
        this.numberOfCounters = numberOfCounters;
    }

//    public List<String> getDocumentNames() {
//        return null;
//    }
//
//    public void setDocumentNames(List<String> documentNames) {
//        this.documentNames = documentNames;
//    }

    public String getName() {return officeName;}
    public void setName(String name) {this.officeName = name;}

}