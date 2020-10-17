package com.mai.nix.maiapp.choose_groups

sealed class GroupsIntent {
    object UpdateGroups : GroupsIntent()
    data class SetFaculty(val faculty: String) : GroupsIntent()
    data class SetCourse(val course: String) : GroupsIntent()
}