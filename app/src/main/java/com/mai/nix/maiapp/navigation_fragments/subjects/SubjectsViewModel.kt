package com.mai.nix.maiapp.navigation_fragments.subjects

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
class SubjectsViewModel(private val subjectsRepository: SubjectsRepository) : ViewModel() {
    val subjectsIntent = Channel<SubjectsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow(SubjectsState(false, "", 0, emptyList(), null))
    val state: StateFlow<SubjectsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            subjectsIntent.consumeAsFlow().collect {
                when (it) {
                    is SubjectsIntent.SetWeek -> setWeek(it.week)
                    is SubjectsIntent.LoadSubjects -> fetchSubjects(it.group)
                    is SubjectsIntent.SetGroup -> setGroup(it.group)
                }
            }
        }
    }

    private fun setWeek(week: Int) {
        viewModelScope.launch {
            _state.value = SubjectsState(
                    _state.value.loading,
                    _state.value.group,
                    week,
                    _state.value.subjects,
                    _state.value.error
            )
            if (_state.value.group.isNotEmpty()) fetchSubjects(_state.value.group)
        }
    }

    private fun setGroup(group: String) {
        viewModelScope.launch {
            _state.value = SubjectsState(
                    _state.value.loading,
                    group,
                    state.value.week,
                    _state.value.subjects,
                    _state.value.error
            )
            fetchSubjects(_state.value.group)
        }
    }

    private fun fetchSubjects(group: String) {
        viewModelScope.launch {
            _state.value = SubjectsState(
                    true,
                    group,
                    _state.value.week,
                    emptyList(),
                    null
            )
            _state.value = try {
                SubjectsState(
                        false,
                        group,
                        _state.value.week,
                        subjectsRepository.getSubjects(group, if (_state.value.week != 0) _state.value.week.toString() else ""),
                        null
                )
            } catch (e: Exception) {
                SubjectsState(false,
                        group,
                        _state.value.week,
                        emptyList(),
                        e.localizedMessage
                )
            }
        }
    }

}