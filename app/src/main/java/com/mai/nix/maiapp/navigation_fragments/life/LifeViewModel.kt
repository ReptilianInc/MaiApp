package com.mai.nix.maiapp.navigation_fragments.life

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
class LifeViewModel(private val lifeRepository: LifeRepository): ViewModel() {
    val studentOrganisationsIntent = Channel<StudentOrganisationsIntent>(Channel.UNLIMITED)
    val sportSectionsIntent = Channel<SportSectionsIntent>(Channel.UNLIMITED)
    val associationsIntent = Channel<AssociationsIntent>(Channel.UNLIMITED)

    private val _studentOrganisationsState = MutableStateFlow(SimpleListState(false, emptyList(), null))
    val studentOrganisationsState: StateFlow<SimpleListState> get() = _studentOrganisationsState

    private val _sportSectionsState = MutableStateFlow(ExpandableListState(false, emptyList(), null))
    val sportSectionsState: StateFlow<ExpandableListState> get() = _sportSectionsState

    private val _associationsState = MutableStateFlow(SimpleListState(false, emptyList(), null))
    val associationsState: StateFlow<SimpleListState> get() = _associationsState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            studentOrganisationsIntent.consumeAsFlow().collect {
                when (it) {
                    StudentOrganisationsIntent.LoadOrganisations -> fetchStudentOrganisations()
                }
            }
        }

        viewModelScope.launch {
            sportSectionsIntent.consumeAsFlow().collect {
                when (it) {
                    SportSectionsIntent.LoadSportSections -> fetchSportSections()
                }
            }
        }

        viewModelScope.launch {
            associationsIntent.consumeAsFlow().collect {
                when (it) {
                    AssociationsIntent.LoadAssociations -> fetchAssociations()
                }
            }
        }
    }

    private fun fetchStudentOrganisations() {
        viewModelScope.launch {
            _studentOrganisationsState.value = SimpleListState(true, emptyList(), null)
            _studentOrganisationsState.value = try {
                SimpleListState(false, lifeRepository.getStudentOrganisations(), null)
            } catch (e: Exception) {
                SimpleListState(false, emptyList(), e.localizedMessage)
            }
        }
    }

    private fun fetchSportSections() {
        viewModelScope.launch {
            _sportSectionsState.value = ExpandableListState(true, emptyList(), null)
            _sportSectionsState.value = try {
                ExpandableListState(false, lifeRepository.getSportSections(), null)
            } catch (e: Exception) {
                ExpandableListState(false, emptyList(), e.localizedMessage)
            }
        }
    }

    private fun fetchAssociations() {
        viewModelScope.launch {
            _associationsState.value = SimpleListState(true, emptyList(), null)
            _associationsState.value = try {
                SimpleListState(false, lifeRepository.getAssociations(), null)
            } catch (e: Exception) {
                SimpleListState(false, emptyList(), e.localizedMessage)
            }
        }
    }
}