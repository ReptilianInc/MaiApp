package com.mai.nix.maiapp.model;

import java.util.UUID;

/**
 * Created by Nix on 02.08.2017.
 */

public class SubjectBody {
    private String mTitle;
    private String mTeacher;
    private String mType;
    private String mTime;
    private String mRoom;
    private UUID mUuid;

    public SubjectBody(String title, String teacher, String type, String time, String room) {
        mTitle = title;
        mTeacher = teacher;
        mType = type;
        mTime = time;
        mRoom = room;
    }
    public SubjectBody() {
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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        mRoom = room;
    }

    public UUID getUuid() {
        return mUuid;
    }

    public void setUuid(UUID uuid) {
        mUuid = uuid;
    }
}
