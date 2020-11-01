package com.mai.nix.maiapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.mai.nix.maiapp.database.AppDatabase
import com.mai.nix.maiapp.helpers.UserSettings

class MaiApp: Application() {

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        UserSettings.initialize(this)
        AppCompatDelegate.setDefaultNightMode(UserSettings.getTheme(this))
        database = Room.databaseBuilder(this, AppDatabase::class.java, "app_database").build()
    }

    fun getDatabase(): AppDatabase {
        return database
    }
}