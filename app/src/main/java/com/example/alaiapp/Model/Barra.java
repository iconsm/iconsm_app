package com.example.alaiapp.Model;

public class Barra {

    private String uid_barra;
    private String day_barra;
    private String start_time_barra;
    private String end_time_barra;
    private String name_barra;

    public Barra() {
    }

    public String getUid_barra() {
        return uid_barra;
    }

    public void setUid_barra(String uid_barra) {
        this.uid_barra = uid_barra;
    }

    public String getDay_barra() {
        return day_barra;
    }

    public void setDay_barra(String day_barra) {
        this.day_barra = day_barra;
    }

    public String getStart_time_barra() {
        return start_time_barra;
    }

    public void setStart_time_barra(String time_barra) {
        this.start_time_barra = time_barra;
    }

    public String getEnd_time_barra() {
        return end_time_barra;
    }

    public void setEnd_time_barra(String time_barra) {
        this.end_time_barra = time_barra;
    }

    public String getName_barra() {
        return name_barra;
    }

    public void setName_barra(String name_barra) {
        this.name_barra = name_barra;
    }

    @Override
    public String toString() {
        return name_barra;
    }
}
