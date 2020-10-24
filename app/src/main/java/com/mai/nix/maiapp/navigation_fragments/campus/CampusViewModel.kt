package com.mai.nix.maiapp.navigation_fragments.campus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mai.nix.maiapp.expandable_list_fragments.ExpandableListState
import com.mai.nix.maiapp.simple_list_fragments.SimpleListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

@ExperimentalCoroutinesApi
class CampusViewModel(private val campusRepository: CampusRepository) : ViewModel() {
    val barracksIntent = Channel<BarracksIntent>(Channel.UNLIMITED)
    val librariesIntent = Channel<LibrariesIntent>(Channel.UNLIMITED)
    val cafesIntent = Channel<CafesIntent>(Channel.UNLIMITED)

    private val _barracksState = MutableStateFlow(ExpandableListState(false, emptyList(), null))
    val barracksState: StateFlow<ExpandableListState> get() = _barracksState

    private val _librariesState = MutableStateFlow(ExpandableListState(false, emptyList(), null))
    val librariesState: StateFlow<ExpandableListState> get() = _librariesState

    private val _cafesState = MutableStateFlow(SimpleListState(false, emptyList(), null))
    val cafesState: StateFlow<SimpleListState> get() = _cafesState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            barracksIntent.consumeAsFlow().collect {
                when (it) {
                    BarracksIntent.BarracksLoad -> fetchBarracks()
                }
            }
        }
        viewModelScope.launch {
            librariesIntent.consumeAsFlow().collect {
                when (it) {
                    LibrariesIntent.LibrariesLoad -> fetchLibraries()
                }
            }
        }
        viewModelScope.launch {
            cafesIntent.consumeAsFlow().collect {
                when (it) {
                    CafesIntent.CafesLoad -> fetchCafes()
                }
            }
        }
    }

    private fun fetchBarracks() {
        viewModelScope.launch {
            _barracksState.value = ExpandableListState(true, emptyList(), null)
            _barracksState.value = try {
                ExpandableListState(false, campusRepository.getBarracks(), null)
            } catch (e: Exception) {
                ExpandableListState(false, emptyList(), e.localizedMessage)
            }
        }
    }

    private fun fetchLibraries() {
        viewModelScope.launch {
            _librariesState.value = ExpandableListState(true, emptyList(), null)
            _librariesState.value = try {
                ExpandableListState(false, campusRepository.getLibraries(), null)
            } catch (e: Exception) {
                ExpandableListState(false, emptyList(), e.localizedMessage)
            }
        }
    }

    private fun fetchCafes() {
        viewModelScope.launch {
            _cafesState.value = SimpleListState(true, emptyList(), null)
            _cafesState.value = try {
                SimpleListState(false, campusRepository.getCafes(), null)
            } catch (e: Exception) {
                SimpleListState(false, emptyList(), e.localizedMessage)
            }
        }
    }

}