package com.mai.nix.maiapp.choose_groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class GroupsViewModelFactory: ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupsViewModel::class.java)) {
            return GroupsViewModel(GroupsRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}