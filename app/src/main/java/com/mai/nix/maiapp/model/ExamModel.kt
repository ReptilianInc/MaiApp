package com.mai.nix.maiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Nix on 01.08.2017.
 */

@Entity
data class ExamModel(
        val date: String,
        val day: String,
        val time: String,
        val title: String,
        val teacher: String,
        val room: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0
) {
    constructor() : this("", "", "", "", "", "")
}