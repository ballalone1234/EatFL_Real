package com.example.eatfl.Model;

import android.text.format.DateUtils;

import com.example.eatfl.View.AppControl;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Public_plan_model extends AppControl {
    private String name;
    private Double money_all,money_spent,money_save;
    private Double cal_to_day;
    private Double pro_to_day;
    private String doc_id;

    private String owner;


    private String duration;

    private Timestamp date;

    private Integer like;
    public Public_plan_model() {
    }
    public Public_plan_model(String name, Double money_all
            , Double cal_to_day, Double pro_to_day
            , String doc_id , String duration
            , Double money_spent , Double money_save
            ,String owner , Timestamp date
             , Integer like) {
        this.name = name;
        this.money_all = money_all;
        this.cal_to_day = cal_to_day;
        this.pro_to_day = pro_to_day;
        this.doc_id = doc_id;
        this.duration = duration;
        this.money_spent = money_spent;
        this.money_save = money_save;
        this.owner = owner;
        this.date = date;
        this.like = like;
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

    public Timestamp getDate() {
        return date;
    }
    public String getFormattedDate() {
        Date date = this.date.toDate(); // Convert Firestore Timestamp to java.util.Date
        long now = System.currentTimeMillis();
        return (String) DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.MINUTE_IN_MILLIS);
    }

    public Integer getLike() {
        if (like == null)
            return 0;
        else
            return like;
    }
}
