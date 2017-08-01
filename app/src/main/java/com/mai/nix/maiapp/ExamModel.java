package com.mai.nix.maiapp;

/**
 * Created by Nix on 01.08.2017.
 */

public class ExamModel {
    protected String mDate;
    protected String mDay;
    protected String mTime;
    protected String mTitle;
    protected String mTeacher;
    protected String mRoom;

    public ExamModel(String date, String day, String time, String title, String teacher, String room) {
        mDate = date;
        mDay = day;
        mTime = time;
        mTitle = title;
        mTeacher = teacher;
        mRoom = room;
    }
    public ExamModel(String date, String day, String time, String title, String room) {
        mDate = date;
        mDay = day;
        mTime = time;
        mTitle = title;
        mRoom = room;
    }
    public ExamModel(){

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

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTeacher() {
        return mTeacher;
    }

    public void setTeacher(String teacher) {
        mTeacher = teacher;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }
}
