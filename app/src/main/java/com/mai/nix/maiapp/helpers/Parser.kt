package com.mai.nix.maiapp.helpers

import android.util.Log
import com.mai.nix.maiapp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements
import java.util.ArrayList

object Parser {

    private const val GROUPS = "groups"
    private const val ASSOCIATIONS = "associations"
    private const val STUDENT_ORGANISATIONS = "student_organisations"
    private const val CAFES = "cafes"
    private const val BARRACKS = "barracks"
    private const val LIBRARIES = "libraries"
    private const val SPORT_SECTIONS = "sport_sections"
    private const val EXAMS = "exams"
    private const val SUBJECTS = "subjects"

    private val links = mapOf(
            GROUPS to "https://mai.ru/education/schedule/?department=",
            ASSOCIATIONS to "https://www.mai.ru/life/associations/",
            STUDENT_ORGANISATIONS to "https://www.mai.ru/life/join/index.php",
            CAFES to "https://mai.ru/common/campus/cafeteria/",
            BARRACKS to "https://mai.ru/common/campus/dormitory.php",
            LIBRARIES to "https://mai.ru/common/campus/library/",
            SPORT_SECTIONS to "http://www.mai.ru/life/sport/sections.php",
            EXAMS to "https://mai.ru/education/schedule/session.php?group=",
            SUBJECTS to "https://mai.ru/education/schedule/detail.php?group="
    )

    fun generateExamsLink(group: String) = links[EXAMS] + group

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

    suspend fun parseAssociations(): List<SimpleListModel> {
        val list = mutableListOf<SimpleListModel>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(links[ASSOCIATIONS]).get()
        }
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
        return list
    }

    suspend fun parseStudentOrganisations(): List<SimpleListModel> {
        val list = mutableListOf<SimpleListModel>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(links[STUDENT_ORGANISATIONS]).get()
        }
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

        return list
    }

    suspend fun parseCafes(): List<SimpleListModel> {
        val list = mutableListOf<SimpleListModel>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(links[CAFES]).get()
        }
        val table = doc?.select("table[class = table table-bordered]")?.first()
        val rows = table?.select("tr") ?: return list
        for (i in 1 until rows.size) {
            val element = rows[i].select("td")
            list.add(SimpleListModel(element[1].text(), element[2].text(), null, element[3].text()))
        }
        return list
    }

    suspend fun parseBarracks(): List<ExpandableItemHeader> {
        val collection = mutableListOf<ExpandableItemHeader>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(links[BARRACKS]).get()
        }
        val table = doc?.select("table[class=data-table]")?.first()
        val stupidHeader = doc?.select("h2")?.first()
        val rows = table?.select("tr")
        val header = table?.select("th")?.first()
        if (table == null) return collection
        collection.add(ExpandableItemHeader(stupidHeader!!.text(), mutableListOf()))
        collection.add(ExpandableItemHeader(header!!.text()!!, mutableListOf()))
        var j = 0
        for (i in rows!!.indices) {
            val el = rows[i].select("td")
            if (!el.isEmpty()) {
                val body = ExpandableItemBody(el[0].select("b").text(),
                        el[0].ownText(),
                        el[1].text())
                collection[j].bodies.add(body)
            } else {
                j++
            }
        }
        return collection
    }

    suspend fun parseLibraries(): List<ExpandableItemHeader> {
        val collection = mutableListOf<ExpandableItemHeader>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(links[LIBRARIES]).get()
        }
        val table = doc?.select("table[class = table table-bordered table-hover]")?.first()
        val rows = doc?.select("tr")
        if (table == null) return collection
        for (i in rows!!.indices) {
            if (rows[i].select("th").size == 1) {
                collection.add(ExpandableItemHeader(rows[i].text(), mutableListOf()))
            }
        }
        var j = 0
        for (i in 2 until rows.size) {
            val el = rows[i].select("td")
            if (!el.isEmpty()) {
                val body = ExpandableItemBody(
                        el[0].text(),
                        el[1].html(),
                        null)
                collection[j].bodies.add(body)
            } else if (rows[i].select("th").size == 1) {
                j++
            }
        }
        return collection
    }

    suspend fun parseSportSections(): List<ExpandableItemHeader> {
        val collection = mutableListOf<ExpandableItemHeader>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(links[SPORT_SECTIONS]).get()
        }
        val table = doc?.select("table[class=data-table]")?.first()
        val rows = table?.select("tr")
        val headers = table?.select("th")
        if (table == null) return collection
        for (i in headers!!.indices) {
            collection.add(ExpandableItemHeader(headers[i].text(), mutableListOf()))
        }
        var j = 0
        for (i in 1 until rows!!.size) {
            val element = rows[i].select("td")
            if (!element.isEmpty()) {
                if (element.size > 2) {
                    val body = ExpandableItemBody(element[0].text(), element[1].text(), getExtractedText(element[2]))
                    collection[j].bodies.add(body)
                } else {
                    val body = ExpandableItemBody(element[0].text(), "", element[1].html())
                    collection[j].bodies.add(body)
                }
            } else {
                j++
            }
        }
        return collection
    }

    private fun getExtractedText(element: Element): String {
        val nodes = element.childNodes()
        val stringBuilder = StringBuilder()
        val iterator: Iterator<Node> = nodes.iterator()
        while (iterator.hasNext()) {
            val n = iterator.next()
            if (n is TextNode) {
                stringBuilder.append(n.text())
            } else if (n is Element) {
                stringBuilder.append(n.attr("href"))
            }
        }
        return stringBuilder.toString()
    }

    suspend fun parseExams(group: String): List<ExamModel> {
        val finalLink = generateExamsLink(group)
        val exams = mutableListOf<ExamModel>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(finalLink).get()
        }
        Log.d("parseExams() link = ", finalLink)
        val date = doc.select("div[class=sc-table-col sc-day-header sc-gray]")
        val day = doc.select("span[class=sc-day]")
        val container = doc.select("div[class=sc-table-col sc-table-detail-container]")
        if (day.isEmpty()) return exams
        for (i in day.indices) {
            val time: Elements = container[i].select("div[class=sc-table-col sc-item-time]")
            val title: Elements = container[i].select("span[class=sc-title]")
            val teacher: Elements = container[i].select("div[class=sc-table-col sc-item-title]")
            val room: Elements = container[i].select("div[class=sc-table-col sc-item-location]")
            for (k in time.indices) {
                val model = ExamModel(date[i].text(), day[i].text(), time[k].text(), title[k].text(),
                        teacher[k].select("span[class=sc-lecturer]").text(), room[k].text())
                exams.add(model)
            }
        }
        return exams
    }

    suspend fun parseSubjects(group: String, week: String = ""): List<Schedule> {
        val finalLink = links[SUBJECTS] + group + "&week=" + week
        val schedules = mutableListOf<Schedule>()
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect(finalLink).get()
        }
        Log.d("parseSubjects() link = ", finalLink)
        val primaries = doc.select("div[class=sc-table sc-table-day]")
        if (primaries.isNullOrEmpty()) {
            return schedules
        }
        primaries.forEach {
            var date = it.select("div[class=sc-table-col sc-day-header sc-gray]").text()
            if (date.isEmpty()) {
                date = it.select("div[class=sc-table-col sc-day-header sc-blue]").text()
            }
            val day = it.select("span[class=sc-day]").text()
            val schedule = Schedule()
            schedule.day = Day(date, day)
            val subjects = ArrayList<Subject>()
            val times = it.select("div[class=sc-table-col sc-item-time]")
            val types = it.select("div[class=sc-table-col sc-item-type]")
            val titles = it.select("span[class=sc-title]")
            val teachers = it.select("div[class=sc-table-col sc-item-title]")
            val rooms = it.select("div[class=sc-table-col sc-item-location]")
            for (i in times.indices) {
                val body = Subject(titles[i].text(),
                        teachers[i].select("span[class=sc-lecturer]").text(),
                        types[i].text(), times[i].text(), rooms[i].text())
                subjects.add(body)
            }
            schedule.subjects = subjects
            schedules.add(schedule)
        }
        return schedules
    }
}