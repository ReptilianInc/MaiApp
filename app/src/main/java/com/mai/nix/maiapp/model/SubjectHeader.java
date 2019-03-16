package com.mai.nix.maiapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nix on 02.08.2017.
 */

@Entity(tableName = "days")
public class SubjectHeader {
    @ColumnInfo(name = "date")
    private String mDate;
    @ColumnInfo(name = "day")
    private String mDay;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;
    @Ignore
    private List<SubjectBody> mChildren;

    public SubjectHeader(String date, String day) {
        mDate = date;
        mDay = day;
        mChildren = new ArrayList<>();
    }

    public SubjectHeader() {
        mChildren = new ArrayList<>();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
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

    public List<SubjectBody> getChildren() {
        return mChildren;
    }

    public void setChildren(List<SubjectBody> children) {
        mChildren = children;
    }

}
