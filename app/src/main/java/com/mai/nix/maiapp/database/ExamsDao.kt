package com.mai.nix.maiapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mai.nix.maiapp.model.ExamModel

@Dao
interface ExamsDao {
    @Query("SELECT * FROM ExamModel")
    suspend fun getExams(): List<ExamModel>

    @Insert
    suspend fun insertExams(exams: List<ExamModel>)

    @Update
    suspend fun updateExams(exams: List<ExamModel>)

    @Query("DELETE FROM ExamModel")
    suspend fun clearAll()
}