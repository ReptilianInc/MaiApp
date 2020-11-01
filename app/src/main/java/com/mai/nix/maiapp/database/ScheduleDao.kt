package com.mai.nix.maiapp.database

import androidx.room.*
import com.mai.nix.maiapp.model.Day
import com.mai.nix.maiapp.model.Schedule
import com.mai.nix.maiapp.model.Subject

@Dao
interface ScheduleDao {
    @Transaction
    @Query("SELECT id, date, day from day")
    suspend fun getSchedulesForWeek(): List<Schedule>

    @Insert
    suspend fun insertDay(day: Day): Long

    @Insert
    suspend fun insertSubject(subject: Subject)

    @Query("DELETE FROM day")
    suspend fun deleteAllDays()

    @Query("DELETE FROM subject")
    suspend fun deleteAllSubjects()

    suspend fun deleteAll() {
        deleteAllDays()
        deleteAllSubjects()
    }

    suspend fun updateAll(schedules: List<Schedule>) {
        deleteAll()
        schedules.forEach { schedule ->
            val id = insertDay(schedule.day?: throw Exception("Schedule day is NULL"))
            schedule.subjects?.forEach {
                it.dayId = id
                insertSubject(it)
            }
        }
    }
}