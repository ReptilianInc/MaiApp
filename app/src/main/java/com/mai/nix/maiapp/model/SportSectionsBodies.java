package com.mai.nix.maiapp.model;

/**
 * Created by Nix on 11.08.2017.
 */

public class SportSectionsBodies {
    private String mTitle;
    private String mOwner;
    private String mPhoneEtc;

    public SportSectionsBodies(String title, String owner, String phoneEtc) {
        mTitle = title;
        mOwner = owner;
        mPhoneEtc = phoneEtc;
    }

    public SportSectionsBodies(String title, String owner){
        mTitle = title;
        mOwner = owner;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOwner() {
        return mOwner;
    }

    public String getPhoneEtc() {
        return mPhoneEtc;
    }

}
