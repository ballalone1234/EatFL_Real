package com.example.eatfl;

import com.google.firebase.Timestamp;

public class Plan_item {
    private String part;
    private Double price;
    private Double amount;
    private String location;
    private Timestamp timestamp;
    private String image;
    private String et;
    public Plan_item() {}
    public Plan_item(String part, Double price, Double amount, String et, String location, String image, Timestamp timestamp) {
        this.part = part;
        this.price = price;
        this.amount = amount;
        this.et = et;
        this.location = location;
        this.image = image;
        this.timestamp = timestamp;
    }

    public String getPart() {
        return part;
    }

    public Double getPrice() {
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

    public String getImage() {
        return image;
    }

    public String getRecipe() {
        String recipe = String.format("%s %s %s",amount.toString(), et, part);
        return recipe;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
