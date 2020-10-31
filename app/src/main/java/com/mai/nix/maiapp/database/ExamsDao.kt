package com.mai.nix.maiapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mai.nix.maiapp.model.ExamModel

@Dao
interface ExamsDao {
    @Query("SELECT * FROM ExamModel")
    suspend fun getExams(): List<ExamModel>

    @Insert
    suspend fun insertExams(exams: List<ExamModel>)
}