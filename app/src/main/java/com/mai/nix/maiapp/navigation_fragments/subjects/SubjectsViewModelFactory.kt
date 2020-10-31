package com.mai.nix.maiapp.navigation_fragments.subjects

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SubjectsViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectsViewModel::class.java)) {
            return SubjectsViewModel(SubjectsRepository(), application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}