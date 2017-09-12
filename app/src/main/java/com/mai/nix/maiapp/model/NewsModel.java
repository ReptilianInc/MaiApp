package com.mai.nix.maiapp.model;


/**
 * Created by Nix on 14.04.2017.
 */

public class NewsModel {
    private String mDate;
    private String mId;
    private String mText;
    private String mLink;

    public NewsModel(String text, String date, String link, String id) {
        mDate = date;
        mText = text;
        mLink = link;
        mId = id;
    }
    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getLink() {
        return mLink;
    }
}
