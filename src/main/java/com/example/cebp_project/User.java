package com.example.cebp_project;

import com.example.cebp_project.*;

import java.util.ArrayList;

public class User {
    private String Name;
    private String Surname;
    public ArrayList<Office> officeList;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }
}
