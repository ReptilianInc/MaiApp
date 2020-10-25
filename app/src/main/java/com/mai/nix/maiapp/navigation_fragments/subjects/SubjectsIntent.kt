package com.mai.nix.maiapp.navigation_fragments.subjects

sealed class SubjectsIntent {
    data class LoadSubjects(val group: String, val week: String): SubjectsIntent()
}