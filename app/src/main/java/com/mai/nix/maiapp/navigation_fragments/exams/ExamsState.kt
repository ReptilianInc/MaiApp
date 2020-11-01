package com.mai.nix.maiapp.navigation_fragments.exams

import com.mai.nix.maiapp.model.ExamModel

data class ExamsState(val loading: Boolean,
                      val group: String,
                      val exams: List<ExamModel>,
                      val error: String?)