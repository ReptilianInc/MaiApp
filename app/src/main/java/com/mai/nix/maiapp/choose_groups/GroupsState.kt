package com.mai.nix.maiapp.choose_groups

sealed class GroupsState {
    object Idle: GroupsState()
    object Loading: GroupsState()
    data class Groups(val groups: List<String>) : GroupsState()
    data class Error(val error: String?) : GroupsState()
}