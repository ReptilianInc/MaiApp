package com.mai.nix.maiapp.expandable_list_fragments

import com.mai.nix.maiapp.model.ExpandableItemHeader

data class ExpandableListState(val loading: Boolean,
                               val items: List<ExpandableItemHeader>,
                               val error: String?)