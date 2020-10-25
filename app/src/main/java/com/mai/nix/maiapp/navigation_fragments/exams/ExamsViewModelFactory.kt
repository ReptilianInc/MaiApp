package com.mai.nix.maiapp.navigation_fragments.exams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ExamsViewModelFactory: ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamsViewModel::class.java)) {
            return ExamsViewModel(ExamsRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}