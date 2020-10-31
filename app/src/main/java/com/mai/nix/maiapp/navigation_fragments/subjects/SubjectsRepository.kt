package com.mai.nix.maiapp.navigation_fragments.subjects

import android.app.Application
import com.mai.nix.maiapp.MaiApp
import com.mai.nix.maiapp.helpers.Parser

class SubjectsRepository {
    suspend fun getSubjectsWeb(group: String, week: String) = Parser.parseSubjects(group, week)
    suspend fun getSubjectsDb(application: Application) = (application as MaiApp).getDatabase().scheduleDao.getSchedulesForWeek()
}