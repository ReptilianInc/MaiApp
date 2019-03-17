package com.mai.nix.maiapp;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.mai.nix.maiapp.room.AppDatabase;

public class MaiApp extends Application {
    private static MaiApp sInstance;

    private AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mAppDatabase = Room.databaseBuilder(this, AppDatabase.class, "mai_database").build();
    }

    public static MaiApp getInstance() {
        return sInstance;
    }

    public AppDatabase getDatabase() {
        return mAppDatabase;
    }
}
