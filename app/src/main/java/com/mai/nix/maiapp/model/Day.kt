package com.mai.nix.maiapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Nix on 02.08.2017.
 */

@Entity
data class Day(
        val date: String,
        val day: String,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0) {

    constructor() : this("", "")
}