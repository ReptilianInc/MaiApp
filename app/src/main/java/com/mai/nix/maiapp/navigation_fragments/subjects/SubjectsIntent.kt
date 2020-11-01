package com.mai.nix.maiapp.navigation_fragments.subjects

sealed class SubjectsIntent {
    data class LoadSubjects(val group: String, val useDb: Boolean): SubjectsIntent()
    data class SetWeek(val week: Int): SubjectsIntent()
    data class SetGroup(val group: String): SubjectsIntent()
}