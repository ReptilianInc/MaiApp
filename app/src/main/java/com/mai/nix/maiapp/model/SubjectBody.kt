package com.mai.nix.maiapp.model

/**
 * Created by Nix on 02.08.2017.
 */
class SubjectBody {
    var title: String? = null
    var teacher: String? = null
    var type: String? = null
    var time: String? = null
    var room: String? = null

    constructor(title: String?, teacher: String?, type: String?, time: String?, room: String?) {
        this.title = title
        this.teacher = teacher
        this.type = type
        this.time = time
        this.room = room
    }

    constructor() {}
}