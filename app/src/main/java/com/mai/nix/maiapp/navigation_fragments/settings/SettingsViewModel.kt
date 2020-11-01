package com.mai.nix.maiapp.navigation_fragments.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class SettingsViewModel(application: Application): AndroidViewModel(application) {
    val intent = Channel<SettingsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow(SettingsState.SettingsIdle)
    val state: StateFlow<SettingsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope
    }
}