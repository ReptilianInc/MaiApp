package com.mai.nix.maiapp.navigation_fragments.life

import com.mai.nix.maiapp.helpers.Parser

class LifeRepository {
    suspend fun getStudentOrganisations() = Parser.parseStudentOrganisations()
    suspend fun getSportSections() = Parser.parseSportSections()
    suspend fun getAssociations() = Parser.parseAssociations()
}