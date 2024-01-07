package com.example.eatfl;

import com.google.firebase.Timestamp;

public class Plan_item {
    private final String part;
    private final String price;
    private final Double amount;
    private final String location;
    private final Timestamp timestamp;

    private final String et;

    public Plan_item(String part, String price, Double amount, String et, String location, Timestamp timestamp) {
        this.part = part;
        this.price = price;
        this.amount = amount;
        this.et = et;
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getPart() {
        return part;
    }

    public String getPrice() {
        return price;
    }

    public Double getAmount() {
        return amount;
    }

    public String getEt() {
        return et;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
