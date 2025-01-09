package com.example.cebp_project;

public class Counter {
    private int id;
    private int officeId;
    private String name;
    private int docId;
    public Counter(int id, int officeId, String name, int docId) {
        this.id = id;
        this.officeId = officeId;
        this.name = name;
        this.docId = docId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }
}
