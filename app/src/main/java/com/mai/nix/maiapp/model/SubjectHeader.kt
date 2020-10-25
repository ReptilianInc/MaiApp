package com.mai.nix.maiapp.model

import java.util.*

/**
 * Created by Nix on 02.08.2017.
 */
class SubjectHeader {
    var date: String? = null
    var day: String? = null
    var children: ArrayList<SubjectBody>

    constructor(date: String?, day: String?) {
        this.date = date
        this.day = day
        children = ArrayList()
    }

    constructor() {
        children = ArrayList()
    }
}