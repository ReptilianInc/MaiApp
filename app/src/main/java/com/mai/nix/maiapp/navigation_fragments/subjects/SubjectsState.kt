package com.mai.nix.maiapp.navigation_fragments.subjects

import com.mai.nix.maiapp.model.SubjectHeader

data class SubjectsState(val loading: Boolean,
                         val group: String,
                         val week: Int,
                         val subjects: List<SubjectHeader>,
                         val error: String?)