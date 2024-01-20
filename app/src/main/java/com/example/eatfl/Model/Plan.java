package com.example.eatfl.Model;

import com.google.firebase.Timestamp;

public class Plan {
    private String name;
    private Timestamp timestamp;



    public Plan(String name, Timestamp timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }
}