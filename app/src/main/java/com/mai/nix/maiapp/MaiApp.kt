package com.mai.nix.maiapp

import android.app.Application
import androidx.room.Room
import com.mai.nix.maiapp.database.AppDatabase

class MaiApp: Application() {

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "app_database").build()
    }

    fun getDatabase(): AppDatabase {
        return database
    }
}