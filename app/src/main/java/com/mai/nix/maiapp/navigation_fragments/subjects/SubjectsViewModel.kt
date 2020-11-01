package com.mai.nix.maiapp.navigation_fragments.subjects

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
class SubjectsViewModel(private val subjectsRepository: SubjectsRepository, app: Application) : AndroidViewModel(app) {
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
                    is SubjectsIntent.SetWeek -> setWeek(it.week, it.useDb)
                    is SubjectsIntent.LoadSubjects -> fetchSubjects(it.group, it.useDb)
                    is SubjectsIntent.SetGroup -> setGroup(it.group, it.useDb)
                    is SubjectsIntent.UpdateSubjects -> fetchSubjects(it.group, it.updateDb)
                }
            }
        }
    }

    private fun setWeek(week: Int, useDb: Boolean) {
        viewModelScope.launch {
            _state.value = SubjectsState(
                    _state.value.loading,
                    _state.value.group,
                    week,
                    _state.value.schedules,
                    _state.value.error
            )
            if (_state.value.group.isNotEmpty()) fetchSubjects(_state.value.group, useDb)
        }
    }

    private fun setGroup(group: String, useDb: Boolean) {
        viewModelScope.launch {
            _state.value = SubjectsState(
                    _state.value.loading,
                    group,
                    state.value.week,
                    _state.value.schedules,
                    _state.value.error
            )
            fetchSubjects(_state.value.group, useDb)
        }
    }

    private fun fetchSubjects(group: String, useDb: Boolean) {
        if (_state.value.week == 0 && useDb) {
            viewModelScope.launch {
                val data = subjectsRepository.getSubjectsDb(getApplication())
                if (data.isNotEmpty()) {
                    Log.d("fetchSubjects($group)", "db is not empty")
                    _state.value = SubjectsState(false, group, _state.value.week, data, null)
                } else {
                    Log.d("fetchSubjects($group)", "db is empty")
                    _state.value = SubjectsState(
                            true,
                            group,
                            _state.value.week,
                            emptyList(),
                            null
                    )
                    try {
                        val dataWeb = subjectsRepository.getSubjectsWeb(group, "")
                        _state.value = SubjectsState(
                                false,
                                group,
                                _state.value.week,
                                dataWeb,
                                null
                        )
                        getApplication<MaiApp>().getDatabase().scheduleDao.updateAll(dataWeb)
                    } catch (e: Exception) {
                        _state.value = SubjectsState(false,
                                group,
                                _state.value.week,
                                emptyList(),
                                e.localizedMessage
                        )
                    }
                }
            }
        } else {
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
                            subjectsRepository.getSubjectsWeb(group, if (_state.value.week != 0) _state.value.week.toString() else ""),
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

}