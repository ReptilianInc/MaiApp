package com.mai.nix.maiapp.model

/**
 * Created by Nix on 01.08.2017.
 */

class ExamModel {
    var date: String? = null
    var day: String? = null
    var time: String? = null
    var title: String? = null
    var teacher: String? = null
    var room: String? = null

    constructor(date: String?, day: String?, time: String?, title: String?, teacher: String?, room: String?) {
        this.date = date
        this.day = day
        this.time = time
        this.title = title
        this.teacher = teacher
        this.room = room
    }

    constructor() {}
}