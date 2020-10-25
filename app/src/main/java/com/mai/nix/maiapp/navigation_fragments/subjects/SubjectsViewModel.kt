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
class SubjectsViewModel(private val subjectsRepository: SubjectsRepository): ViewModel() {
    val subjectsIntent = Channel<SubjectsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow(SubjectsState(false, "", "", emptyList(), null))
    val state: StateFlow<SubjectsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            subjectsIntent.consumeAsFlow().collect {
                when(it) {
                    is SubjectsIntent.LoadSubjects  -> fetchSubjects(it.group, it.week)
                }
            }
        }
    }

    private fun fetchSubjects(group: String, week: String) {
        viewModelScope.launch {
            _state.value = SubjectsState(true, group, week, emptyList(), null)
            _state.value = try {
                SubjectsState(false, group, week, subjectsRepository.getSubjects(group, week), null)
            } catch (e: Exception) {
                SubjectsState(false, group, week, emptyList(), e.localizedMessage)
            }
        }
    }

}