package com.example.cebp_project;

public class Document {
    private final String name;
    private int id;
    private String description;

    public Document(int id, String name, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}
}
