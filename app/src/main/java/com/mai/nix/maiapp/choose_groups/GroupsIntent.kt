package com.mai.nix.maiapp.choose_groups

sealed class GroupsIntent {
    data class FetchGroups(val faculty: String, val course: String) : GroupsIntent()
}