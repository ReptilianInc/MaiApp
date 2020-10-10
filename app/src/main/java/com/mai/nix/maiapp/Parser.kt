package com.mai.nix.maiapp

import android.util.Log
import org.jsoup.Jsoup
import java.io.IOException

object Parser {
    private val links = mapOf("groups" to "http://mai.ru/education/schedule/?department=")

    fun parseGroups(facultyCode: String, currentCourse: Int): List<String> {
        val groups = mutableListOf<String>()
        try {
            val doc = Jsoup.connect(links["groups"] + facultyCode + "&course=" + currentCourse).get()
            Log.d("parseGroups() link = ", links["groups"] + facultyCode + "&course=" + currentCourse)
            val titles = doc.select("a[class = sc-group-item]") ?: return groups
            titles.forEach {
                groups.add(it.text())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return groups
    }
}