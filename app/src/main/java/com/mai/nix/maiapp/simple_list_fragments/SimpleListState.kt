package com.mai.nix.maiapp.simple_list_fragments

import com.mai.nix.maiapp.model.SimpleListModel

class SimpleListState(val loading: Boolean,
                      val items: List<SimpleListModel>,
                      val error: String?)