package com.mai.nix.maiapp.choose_groups

import com.mai.nix.maiapp.helpers.Parser

class GroupsRepository {
    suspend fun getGroups(facultyCode: String, course: String) = Parser.parseGroups(facultyCode, course)
}