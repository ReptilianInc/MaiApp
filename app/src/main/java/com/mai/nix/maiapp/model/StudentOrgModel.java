package com.mai.nix.maiapp.model;

/**
 * Created by Nix on 10.08.2017.
 */

public class StudentOrgModel {
    private String mTitle;
    private String mLeader;
    private String mPhone;
    private String mAddress;

    public StudentOrgModel(String title, String leader, String phone, String address) {
        mTitle = title;
        mLeader = leader;
        mPhone = phone;
        mAddress = address;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLeader() {
        return mLeader;
    }

    public void setLeader(String leader) {
        mLeader = leader;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}
