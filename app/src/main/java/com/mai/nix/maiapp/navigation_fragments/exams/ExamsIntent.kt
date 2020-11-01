package com.mai.nix.maiapp.navigation_fragments.exams

sealed class ExamsIntent {
    data class LoadExams(val group: String, val useDb: Boolean): ExamsIntent()
    data class UpdateExams(val group: String, val updateDb: Boolean): ExamsIntent()
}