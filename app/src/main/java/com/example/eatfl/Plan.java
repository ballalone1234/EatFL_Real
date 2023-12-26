package com.example.eatfl;

import com.google.firebase.Timestamp;

public class Plan {
    private String name;
    private String part;
    private String price;
    private String amount;
    private Timestamp timestamp;

    public Plan(String name, String part, String price, String amount, Timestamp timestamp) {
        this.name = name;
        this.part = part;
        this.price = price;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getPart() {
        return part;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }
}