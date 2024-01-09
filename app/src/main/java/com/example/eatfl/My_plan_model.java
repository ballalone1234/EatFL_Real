package com.example.eatfl;

public class My_plan_model {
    private String name;
    private Double money_all;
    private Double cal_to_day;
    private Double pro_to_day;

    public My_plan_model() {
    }
    public My_plan_model(String name, Double money_all, Double cal_to_day, Double pro_to_day) {
        this.name = name;
        this.money_all = money_all;
        this.cal_to_day = cal_to_day;
        this.pro_to_day = pro_to_day;
    }

    public String getName() {
        return name;
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
}
