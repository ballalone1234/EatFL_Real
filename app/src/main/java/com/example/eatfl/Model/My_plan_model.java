package com.example.eatfl.Model;



public class My_plan_model  {
    private String name;
    private Double money_all,money_spent,money_save;
    private Double cal_to_day;
    private Double pro_to_day;
    private String doc_id;

    private String owner;

    private String duration;
    public My_plan_model() {
    }
    public My_plan_model(String name, Double money_all, Double cal_to_day, Double pro_to_day , String doc_id , String duration , Double money_spent , Double money_save) {
        this.name = name;
        this.money_all = money_all;
        this.cal_to_day = cal_to_day;
        this.pro_to_day = pro_to_day;
        this.doc_id = doc_id;
        this.duration = duration;
        this.money_spent = money_spent;
        this.money_save = money_save;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }
    public Double getMoney_all() {
        return money_all;
    }

    public Double getCal_to_day() {
        return cal_to_day;
    }

    public Double getPro_to_day() {
        return pro_to_day;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public Double getMoney_spent() {
        return money_spent;
    }

    public Double getMoney_save() {
        return money_save;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
