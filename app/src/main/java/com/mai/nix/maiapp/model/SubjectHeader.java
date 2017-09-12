package com.mai.nix.maiapp.model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Nix on 02.08.2017.
 */

public class SubjectHeader {
    private String mDate;
    private String mDay;
    private UUID mUuid;
    private ArrayList<SubjectBody> mChildren;
    public SubjectHeader(String date, String day) {
        this(UUID.randomUUID());
        mDate = date;
        mDay = day;
        mChildren = new ArrayList<>();
    }
    public SubjectHeader(UUID id){
        mUuid = id;
        mChildren = new ArrayList<>();
    }
    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public ArrayList<SubjectBody> getChildren() {
        return mChildren;
    }

    public void setChildren(ArrayList<SubjectBody> children) {
        mChildren = children;
    }

    public UUID getUuid() {
        return mUuid;
    }
}
