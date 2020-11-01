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

    private val _state = MutableStateFlow(ExamsState(false, "", emptyList(), null))
    val state: StateFlow<ExamsState> get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            examsIntent.consumeAsFlow().collect {
                when (it) {
                    is ExamsIntent.LoadExams -> fetchExams(it.group, it.useDb)
                    is ExamsIntent.UpdateExams -> updateExams(it.group, it.updateDb)
                }
            }
        }
    }

    private fun fetchExams(group: String, useDb: Boolean) {
        if (useDb) fetchExamsWithDb(group) else fetchExamsOnlyWeb(group)
    }

    private fun updateExams(group: String, updateDb: Boolean) {
        viewModelScope.launch {
            _state.value = ExamsState(true, group, _state.value.exams, null)
            try {
                val data = examsRepository.getExamsWeb(group)
                _state.value = ExamsState(false, group, data, null)
                if (updateDb && data.isNotEmpty()) {
                    getApplication<MaiApp>().getDatabase().examDao.updateExams(data)
                }
            } catch (e: Exception) {
                _state.value = ExamsState(false, group, emptyList(), e.localizedMessage)
            }
        }
    }

    private fun fetchExamsWithDb(group: String) {
        viewModelScope.launch {
            val examsDb = examsRepository.getExamsDb(getApplication())
            if (examsDb.isNotEmpty()) {
                Log.d("fetchExamsWithDb($group)", "db is not empty")
                _state.value = ExamsState(false, group, examsDb, null)
            } else {
                Log.d("fetchExamsWithDb($group)", "db is empty")
                _state.value = ExamsState(true, group, emptyList(), null)
                try {
                    val examsWeb = examsRepository.getExamsWeb(group)
                    _state.value = ExamsState(false, group, examsWeb, null)
                    if (examsWeb.isNotEmpty()) getApplication<MaiApp>().getDatabase().examDao.insertExams(examsWeb)
                } catch (e: Exception) {
                    _state.value = ExamsState(false, group, emptyList(), e.localizedMessage)
                }
            }
        }
    }

    private fun fetchExamsOnlyWeb(group: String) {
        viewModelScope.launch {
            _state.value = ExamsState(true, group, emptyList(), null)
            _state.value = try {
                ExamsState(false, group, examsRepository.getExamsWeb(group), null)
            } catch (e: Exception) {
                ExamsState(false, group, emptyList(), e.localizedMessage)
            }
        }
    }
}