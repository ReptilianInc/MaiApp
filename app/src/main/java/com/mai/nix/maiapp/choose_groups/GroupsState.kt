package com.mai.nix.maiapp.choose_groups

data class GroupsState (
    val loading: Boolean,
    val groups: List<String>,
    val error: String?
)