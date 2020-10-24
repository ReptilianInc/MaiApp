package com.mai.nix.maiapp.navigation_fragments.campus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CampusViewModelFactory: ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CampusViewModel::class.java)) {
            return CampusViewModel(CampusRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}