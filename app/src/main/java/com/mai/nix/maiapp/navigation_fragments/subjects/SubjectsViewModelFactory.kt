package com.mai.nix.maiapp.navigation_fragments.subjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SubjectsViewModelFactory: ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectsViewModel::class.java)) {
            return SubjectsViewModel(SubjectsRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}