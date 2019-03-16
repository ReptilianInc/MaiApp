package com.mai.nix.maiapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Nix on 02.08.2017.
 */

@Entity(tableName = "subjects")
public class SubjectBody {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "teacher")
    private String mTeacher;
    @ColumnInfo(name = "type")
    private String mType;
    @ColumnInfo(name = "time")
    private String mTime;
    @ColumnInfo(name = "room")
    private String mRoom;
    @ColumnInfo(name = "subject_id")
    private long mSubjectId;

    public SubjectBody(String title, String teacher, String type, String time, String room) {
        mTitle = title;
        mTeacher = teacher;
        mType = type;
        mTime = time;
        mRoom = room;
    }

    public SubjectBody() {
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
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

    public long getSubjectId() {
        return mSubjectId;
    }

    public void setSubjectId(long id) {
        mSubjectId = id;
    }
}
