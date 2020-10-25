package com.mai.nix.maiapp.navigation_fragments.exams

sealed class ExamsIntent {
    data class LoadExams(val group: String): ExamsIntent()
}