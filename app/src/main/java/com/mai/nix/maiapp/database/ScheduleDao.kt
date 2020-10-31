package com.mai.nix.maiapp.database

import androidx.room.*
import com.mai.nix.maiapp.model.Schedule

@Dao
interface ScheduleDao {
    @Transaction
    @Query("SELECT id, date, day from day")
    fun getSchedulesForWeek(): List<Schedule>
}