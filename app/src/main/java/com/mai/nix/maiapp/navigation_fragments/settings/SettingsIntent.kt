package com.mai.nix.maiapp.navigation_fragments.settings

sealed class SettingsIntent {
    object ClearExamsCacheIntent: SettingsIntent()
    object ClearSubjectsCacheIntent: SettingsIntent()
}