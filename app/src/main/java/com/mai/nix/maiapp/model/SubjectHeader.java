package com.mai.nix.maiapp.model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Nix on 02.08.2017.
 */

public class SubjectHeader {
    private String date;
    private String day;
    private UUID uuid;
    // ArrayList to store child objects
    private ArrayList<SubjectBody> children;
    public SubjectHeader(String date, String day) {
        this(UUID.randomUUID());
        this.date = date;
        this.day = day;
        children = new ArrayList<>();
    }
    public SubjectHeader(){
        this(UUID.randomUUID());
        children = new ArrayList<>();
    }
    public SubjectHeader(UUID id){
        uuid = id;
        children = new ArrayList<>();
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

    public ArrayList<SubjectBody> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<SubjectBody> children) {
        this.children = children;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
