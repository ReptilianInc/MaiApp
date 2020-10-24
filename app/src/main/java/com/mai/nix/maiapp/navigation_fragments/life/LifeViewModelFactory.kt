package com.mai.nix.maiapp.navigation_fragments.life

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class LifeViewModelFactory: ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LifeViewModel::class.java)) {
            return LifeViewModel(LifeRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
