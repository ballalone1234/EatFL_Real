package com.example.eatfl.Model;

public class Item {
    private String name;
    private String description;
    private Double price;
    private String image;
    private String part;

    private String location;

    public Item(String name, String description, Double price, String image, String part , String location) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.part = part;
        this.location = location;
    }

    // getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getPart() {
        return part;
    }

    public String getLocation() {
        return location;
    }
}