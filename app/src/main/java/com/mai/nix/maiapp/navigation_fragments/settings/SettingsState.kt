package com.mai.nix.maiapp.navigation_fragments.settings

sealed class SettingsState {
    object SettingsIdle: SettingsState()
    data class ExamsCacheCleared(val success: Boolean, val error: String?): SettingsState()
    data class SubjectsCacheCleared(val success: Boolean, val error: String?): SettingsState()
}