package com.mai.nix.maiapp.choose_groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

@ExperimentalCoroutinesApi
class GroupsViewModel(private val groupsRepository: GroupsRepository): ViewModel() {
    val groupsIntent = Channel<GroupsIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<GroupsState>(GroupsState.Idle)
    val state : StateFlow<GroupsState> get() = _state

    init {
        handleGroups()
    }

    private fun handleGroups() {
        viewModelScope.launch {
            groupsIntent.consumeAsFlow().collect {
                when(it) {
                    is GroupsIntent.FetchGroups -> fetchGroups(it.faculty, it.course)
                }
            }
        }
    }

    private fun fetchGroups(facultyCode: String, course: String) {
        viewModelScope.launch {
            _state.value = GroupsState.Loading
            _state.value = try {
                GroupsState.Groups(groupsRepository.getGroups(facultyCode, course))
            } catch (e: Exception) {
                GroupsState.Error(e.localizedMessage)
            }
        }
    }
}