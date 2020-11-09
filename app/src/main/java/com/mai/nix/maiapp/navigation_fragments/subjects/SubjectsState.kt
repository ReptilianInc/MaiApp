package com.mai.nix.maiapp.navigation_fragments.subjects

import com.mai.nix.maiapp.model.Schedule

data class SubjectsState(val loading: Boolean,
                         val cacheUpdated: Boolean,
                         val group: String,
                         val week: Int,
                         val schedules: List<Schedule>,
                         val error: String?)