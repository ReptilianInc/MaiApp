package com.mai.nix.maiapp.navigation_fragments.subjects

import com.mai.nix.maiapp.helpers.Parser

class SubjectsRepository {
    suspend fun getSubjects(group: String, week: String) = Parser.parseSubjects(group, week)
}