package com.mai.nix.maiapp.navigation_fragments.exams

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mai.nix.maiapp.MaiApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

@ExperimentalCoroutinesApi
class ExamsViewModel(private val examsRepository: ExamsRepository, application: Application) : AndroidViewModel(application) {
    val examsIntent = Channel<ExamsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow(ExamsState(false, emptyList(), null))
    val state: StateFlow<ExamsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            examsIntent.consumeAsFlow().collect {
                when (it) {
                    is ExamsIntent.LoadExams -> fetchExams(it.group)
                }
            }
        }
    }

    private fun fetchExams(group: String) {
        viewModelScope.launch {
            val exams = examsRepository.getExamsDb(getApplication())
            if (exams.isNotEmpty()) {
                Log.d("Exams", "db is not empty")
                _state.value = ExamsState(false, exams, null)
            } else {
                Log.d("Exams", "db is empty")
                _state.value = ExamsState(true, emptyList(), null)
                try {
                    val exams1 = examsRepository.getExamsWeb(group)
                    _state.value = ExamsState(false, exams1, null)
                    getApplication<MaiApp>().getDatabase().examDao.insertExams(exams1)
                } catch (e: Exception) {
                    _state.value = ExamsState(false, emptyList(), e.localizedMessage)
                }
            }
        }
    }
}