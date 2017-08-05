package com.mai.nix.maiapp.model;

import java.util.ArrayList;

/**
 * Created by Nix on 02.08.2017.
 */

public class SubjectHeaders {
    private String date;
    private String day;
    // ArrayList to store child objects
    private ArrayList<SubjectBodies> children;
    public SubjectHeaders(String date, String day) {
        this.date = date;
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<SubjectBodies> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SubjectBodies> children) {
        this.children = children;
    }
}
