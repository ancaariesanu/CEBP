package com.example.cebp_project.Request;

public class CreateCounterRequest {
    private int id;
    private String name;
    private int docId;

    public int getOfficeId() {return id;}

    public void setOfficeId(int id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public int getDocId() {return docId;}

    public void setDocId(int docId) {this.docId = docId;}
}
