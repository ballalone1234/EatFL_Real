package com.example.eatfl;

import com.google.firebase.Timestamp;
import com.google.type.Money;

public class PlanTopic {
    private String name;
    private Timestamp date;
    private String duration;
    private Double money_all;
    private Double money_spent;
    private Double money_save;
    private String owner;
    private String permission;
    private Double cal_to_day;
    private Double pro_to_day;

    public PlanTopic() {}

    public PlanTopic(String name, Timestamp date, String duration, Double money_all, Double money_spent, Double money_save, String owner,String permission, Double cal_to_day, Double pro_to_day) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.money_all = money_all;
        this.money_spent = money_spent;
        this.money_save = money_save;
        this.owner = owner;
        this.permission = permission;
        this.cal_to_day = cal_to_day;
        this.pro_to_day = pro_to_day;
    }

    public String getName() {
        return name;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public Double getMoney_all() {
        return money_all;
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

    public String getPermission() {
        return permission;
    }

    public Double getCal_to_day() {
        return cal_to_day;
    }

    public Double getPro_to_day() {
        return pro_to_day;
    }



}
