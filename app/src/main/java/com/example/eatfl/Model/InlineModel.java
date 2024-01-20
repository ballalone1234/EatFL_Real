package com.example.eatfl.Model;

import java.util.ArrayList;

public class InlineModel {
    private String title;

    private ArrayList<String> ingr;

    public InlineModel() {}

    public InlineModel(String title, ArrayList<String> ingr) {
        this.title = title;
        this.ingr = ingr;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getIngr() {
        return ingr;
    }
}
