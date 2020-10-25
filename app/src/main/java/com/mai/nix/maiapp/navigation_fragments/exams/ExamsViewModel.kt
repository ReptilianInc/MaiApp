package com.mai.nix.maiapp.navigation_fragments.exams

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
class ExamsViewModel(private val examsRepository: ExamsRepository) : ViewModel() {
    val examsIntent = Channel<ExamsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow(ExamsState(false, emptyList(), null))
    val state: StateFlow<ExamsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            examsIntent.consumeAsFlow().collect {
                when(it) {
                    is ExamsIntent.LoadExams -> fetchExams(it.group)
                }
            }
        }
    }

    private fun fetchExams(group: String) {
        viewModelScope.launch {
            _state.value = ExamsState(true, emptyList(), null)
            _state.value = try {
                ExamsState(false, examsRepository.getExams(group), null)
            } catch (e: Exception) {
                ExamsState(false, emptyList(), e.localizedMessage)
            }
        }
    }
}