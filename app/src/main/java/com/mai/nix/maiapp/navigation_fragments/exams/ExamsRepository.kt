package com.mai.nix.maiapp.navigation_fragments.exams

import com.mai.nix.maiapp.helpers.Parser

class ExamsRepository {
    suspend fun getExams(group: String) = Parser.parseExams(group)
}