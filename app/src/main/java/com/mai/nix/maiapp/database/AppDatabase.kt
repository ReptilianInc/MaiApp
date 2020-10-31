package com.mai.nix.maiapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mai.nix.maiapp.model.Day
import com.mai.nix.maiapp.model.Subject

@Database(entities = [Day::class, Subject::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val scheduleDao: ScheduleDao
}