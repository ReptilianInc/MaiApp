package com.mai.nix.maiapp;

/**
 * Created by Nix on 01.08.2017.
 */

public class SubjectModel extends ExamModel {
    private String mType;

    public SubjectModel(String date, String day, String time, String title, String teacher, String room, String type) {
        super(date, day, time, title, teacher, room);
        mType = type;
    }
    public SubjectModel(String time, String title, String teacher, String room, String type) {
        mTime = time;
        mTitle = title;
        mTeacher = teacher;
        mRoom = room;
        mType = type;
    }
    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
