package com.mai.nix.maiapp.helpers

import android.util.Log
import com.mai.nix.maiapp.model.SimpleListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

object Parser {

    private const val GROUPS = "groups"
    private const val ASSOCIATIONS = "associations"
    private const val STUDENT_ORGANISATIONS = "student_organisations"

    private val links = mapOf(
            GROUPS to "http://mai.ru/education/schedule/?department=",
            ASSOCIATIONS to "http://www.mai.ru/life/associations/",
            STUDENT_ORGANISATIONS to "http://www.mai.ru/life/join/index.php"
    )

    suspend fun parseGroups(facultyCode: String, currentCourse: String): List<String> {
        val connectLink = links[GROUPS] + facultyCode + "&course=" + currentCourse
        val groups = mutableListOf<String>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(connectLink).get()
        }
        Log.d("parseGroups() link = ", connectLink)
        val titles = doc.select("a[class = sc-group-item]") ?: return groups
        titles.forEach {
            groups.add(it.text())
        }
        return groups
    }

    fun parseAssociations(): List<SimpleListModel> {
        val list = mutableListOf<SimpleListModel>()
        try {
            val doc = Jsoup.connect(links[ASSOCIATIONS]).get()
            val table = doc?.select("table[class=data-table]")?.first()
            val rows = table?.select("th")
            val cols = table?.select("td")
            if (rows == null || cols == null) return list
            var i = 0
            var j = 0
            while (j < cols.size) {
                list.add(SimpleListModel(rows[i].text(), cols[j].text(), cols[j + 1].text(),
                        cols[j + 2].text()))
                i++
                j += 3
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return list
        }
        return list
    }

    fun parseStudentOrganisations(): List<SimpleListModel> {
        val list = mutableListOf<SimpleListModel>()
        try {
            val doc = Jsoup.connect(links[STUDENT_ORGANISATIONS]).get()
            val table = doc?.select("table[class=data-table]")?.first()
            val rows = table?.select("th")
            val cols = table?.select("td")
            if (rows == null || cols == null) return list
            var i = 0
            var j = 0
            while (j < cols.size) {
                list.add(SimpleListModel(rows[i].text(), cols[j].text(), cols[j + 1].text(),
                        cols[j + 2].text()))
                i++
                j += 3
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return list
        }
        return list
    }
}