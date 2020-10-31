package com.mai.nix.maiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Nix on 02.08.2017.
 */

@Entity
data class Subject(
        val title: String,
        val teacher: String,
        val type: String,
        val time: String,
        val room: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        var dayId: Long = 0) {

    constructor() : this("", "", "", "", "")
}