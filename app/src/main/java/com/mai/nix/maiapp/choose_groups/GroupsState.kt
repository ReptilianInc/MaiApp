package com.mai.nix.maiapp.choose_groups

data class GroupsState (
    val loading: Boolean,
    val groups: List<String>,
    val error: String?,
    val faculty: String,
    val course: String
) {
    fun isValid() = faculty.isNotEmpty() && course.isNotEmpty()
}