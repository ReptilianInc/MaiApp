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
class GroupsViewModel(private val groupsRepository: GroupsRepository) : ViewModel() {
    val intent = Channel<GroupsIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow(GroupsState(
            false,
            arrayListOf(),
            null,
            "",
            "",
            "",
            -1
    ))
    val state: StateFlow<GroupsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is GroupsIntent.SetFaculty -> setFaculty(it.faculty)
                    is GroupsIntent.SetCourse -> setCourse(it.course)
                    is GroupsIntent.UpdateGroups -> fetchData()
                    is GroupsIntent.SetChosenGroup -> setChosenGroup(it.group, it.index)
                }
            }
        }
    }

    private fun setChosenGroup(chosenGroup: String, index: Int) {
        viewModelScope.launch {
            _state.value = GroupsState(
                    _state.value.loading,
                    _state.value.groups,
                    _state.value.error,
                    _state.value.faculty,
                    _state.value.course,
                    chosenGroup,
                    index
            )
        }
    }

    private fun setFaculty(faculty: String) {
        viewModelScope.launch {
            _state.value = GroupsState(
                    false,
                    _state.value.groups,
                    _state.value.error,
                    faculty,
                    _state.value.course,
                    _state.value.chosenGroup,
                    _state.value.index
            )
            if (_state.value.isValid()) {
                fetchData()
            }
        }
    }

    private fun setCourse(course: String) {
        viewModelScope.launch {
            _state.value = GroupsState(
                    false,
                    _state.value.groups,
                    _state.value.error,
                    _state.value.faculty,
                    course,
                    _state.value.chosenGroup,
                    _state.value.index
            )
            if (_state.value.isValid()) {
                fetchData()
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            _state.value = GroupsState(
                    true,
                    _state.value.groups,
                    null,
                    _state.value.faculty,
                    _state.value.course,
                    _state.value.chosenGroup,
                    _state.value.index
            )
            _state.value = try {
                GroupsState(
                        false,
                        groupsRepository.getGroups(_state.value.faculty, _state.value.course),
                        null,
                        _state.value.faculty,
                        _state.value.course,
                        _state.value.chosenGroup,
                        _state.value.index
                )
            } catch (e: Exception) {
                GroupsState(
                        false,
                        arrayListOf(),
                        e.localizedMessage,
                        _state.value.faculty,
                        _state.value.course,
                        _state.value.chosenGroup,
                        _state.value.index
                )
            }
        }
    }
}