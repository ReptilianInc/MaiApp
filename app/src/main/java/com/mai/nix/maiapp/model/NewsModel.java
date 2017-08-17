package com.mai.nix.maiapp.model;

import android.graphics.Bitmap;

/**
 * Created by Nix on 14.04.2017.
 */

public class NewsModel {
    private String mDate;
    private String mImagePath;
    private String mText;
    private Bitmap mBitmap;
    public NewsModel(String text, String date, Bitmap bitmap) {
        mDate = date;
        mBitmap = bitmap;
        mText = text;
    }
    public NewsModel(String text){
        mText = text;
    }
    public NewsModel(String text, String sec_text){
        mText = text;
        mDate = sec_text;
    }
    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        mImagePath = imagePath;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
