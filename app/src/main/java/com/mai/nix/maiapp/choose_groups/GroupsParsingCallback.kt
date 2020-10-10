package com.mai.nix.maiapp.choose_groups

interface GroupsParsingCallback {
    fun startLoad()
    fun endLoad(groups: List<String>)
}