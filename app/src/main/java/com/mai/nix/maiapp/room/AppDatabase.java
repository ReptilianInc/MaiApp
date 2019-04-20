package com.mai.nix.maiapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mai.nix.maiapp.model.SubjectBody;
import com.mai.nix.maiapp.model.SubjectHeader;

@Database(entities = {SubjectHeader.class, SubjectBody.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SubjectsDao subjectsDao();
}
