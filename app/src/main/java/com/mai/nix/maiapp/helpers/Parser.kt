package com.mai.nix.maiapp.helpers

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

object Parser {
    private val links = mapOf("groups" to "http://mai.ru/education/schedule/?department=")

    suspend fun parseGroups(facultyCode: String, currentCourse: String): List<String> {
        val groups = mutableListOf<String>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(links["groups"] + facultyCode + "&course=" + currentCourse).get()
        }
        Log.d("parseGroups() link = ", links["groups"] + facultyCode + "&course=" + currentCourse)
        val titles = doc.select("a[class = sc-group-item]") ?: return groups
        titles.forEach {
            groups.add(it.text())
        }
        return groups
    }
}