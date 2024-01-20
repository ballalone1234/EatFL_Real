package com.example.eatfl.Model;

public class Place {
    private String name;
    private String distace;
    private Double price;


    public Place(String name, String distace, Double price) {
        this.name = name;
        this.distace = distace;
        this.price = price;

    }

    // getters
    public String getName() {
        return name;
    }
    public String getDistace() {
        return distace;
    }
    public Double getPrice() {
        return price;
    }
}