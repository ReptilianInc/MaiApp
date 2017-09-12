package com.mai.nix.maiapp.model;

import java.util.ArrayList;

/**
 * Created by Nix on 11.08.2017.
 */

public class SportSectionsHeaders {
    private String mTitle;
    private ArrayList<SportSectionsBodies> mBodies;

    public SportSectionsHeaders(String title) {
        mTitle = title;
        mBodies = new ArrayList<>();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<SportSectionsBodies> getBodies() {
        return mBodies;
    }

    public void addBody(SportSectionsBodies body){
        mBodies.add(body);
    }
}
