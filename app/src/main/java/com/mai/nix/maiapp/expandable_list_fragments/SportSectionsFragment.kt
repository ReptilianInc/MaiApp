package com.mai.nix.maiapp.expandable_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.ExpandableItemBody
import com.mai.nix.maiapp.model.ExpandableItemHeader
import kotlinx.android.synthetic.main.fragment_expandable_list.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.io.IOException

/**
 * Created by Nix on 11.08.2017.
 */
class SportSectionsFragment : ExpandableListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<Int?, Void?, List<ExpandableItemHeader>>() {

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

        override fun doInBackground(vararg p0: Int?): List<ExpandableItemHeader> {
            val collection = mutableListOf<ExpandableItemHeader>()
            try {
                val doc = Jsoup.connect("http://www.mai.ru/life/sport/sections.php").get()
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
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (n: NullPointerException) {
                return collection
            }
            return collection
        }

        override fun onPostExecute(list: List<ExpandableItemHeader>) {
            expandableListSwipeRefreshLayout.isRefreshing = false
            if (list.isEmpty()) {
                if (context != null) Toast.makeText(context, R.string.error,
                        Toast.LENGTH_LONG).show()
            } else {
                adapter.models.addAll(list)
                adapter.notifyDataSetChanged()
            }
        }
    }
}