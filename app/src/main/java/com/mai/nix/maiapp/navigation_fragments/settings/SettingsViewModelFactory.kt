package com.mai.nix.maiapp.navigation_fragments.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}