package com.mai.nix.maiapp.navigation_fragments.exams

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ExamsViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamsViewModel::class.java)) {
            return ExamsViewModel(ExamsRepository(), application) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}