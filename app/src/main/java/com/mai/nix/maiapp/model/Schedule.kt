package com.mai.nix.maiapp.model

import androidx.room.Embedded
import androidx.room.Relation

class Schedule {
    @Embedded
    var day: Day? = null

    @Relation(parentColumn = "id", entityColumn = "dayId")
    var subjects: List<Subject>? = null
}