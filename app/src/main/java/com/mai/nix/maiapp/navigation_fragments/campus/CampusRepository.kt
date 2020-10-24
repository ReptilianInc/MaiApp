package com.mai.nix.maiapp.navigation_fragments.campus

import com.mai.nix.maiapp.helpers.Parser

class CampusRepository {
    suspend fun getBarracks() = Parser.parseBarracks()
    suspend fun getLibraries() = Parser.parseLibraries()
    suspend fun getCafes() = Parser.parseCafes()
}