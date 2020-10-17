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
    val intent = Channel<GroupsIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow(GroupsState(loading = false, groups = arrayListOf(), error = null))
    val state : StateFlow<GroupsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when(it) {
                    is GroupsIntent.FetchGroups -> fetchData(it.faculty, it.course)
                }
            }
        }
    }

    private fun fetchData(facultyCode: String, course: String) {
        viewModelScope.launch {
            _state.value = GroupsState(loading = true, groups = _state.value.groups, error = null)
            _state.value = try {
                GroupsState(loading = false, groupsRepository.getGroups(facultyCode, course), error = null)
            } catch (e: Exception) {
                GroupsState(loading = false, arrayListOf(), error = e.localizedMessage)
            }
        }
    }
}