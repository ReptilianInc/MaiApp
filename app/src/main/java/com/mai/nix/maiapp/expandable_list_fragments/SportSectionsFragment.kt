package com.mai.nix.maiapp.expandable_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.ExpandableItemBody
import com.mai.nix.maiapp.model.ExpandableItemHeader
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements
import java.io.IOException

/**
 * Created by Nix on 11.08.2017.
 */
class SportSectionsFragment : ExpandableListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<Int?, Void?, Int>() {
        private var doc: Document? = null
        private var table: Element? = null
        private var rows: Elements? = null
        private var headers: Elements? = null
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

        override fun doInBackground(vararg p0: Int?): Int {
            var size = 0
            try {
                doc = Jsoup.connect("http://www.mai.ru/life/sport/sections.php").get()
                table = doc?.select("table[class=data-table]")?.first()
                rows = table?.select("tr")
                headers = table?.select("th")
                if (table != null) mHeaders!!.clear()
                for (i in headers!!.indices) {
                    mHeaders!!.add(ExpandableItemHeader(headers!!.get(i).text(), mutableListOf()))
                }
                var j = 0
                for (i in 1 until rows!!.size) {
                    val el = rows!!.get(i).select("td")
                    if (!el.isEmpty()) {
                        if (el.size > 2) {
                            val body = ExpandableItemBody(el[0].text(), el[1].text(), getExtractedText(el[2]))
                            mHeaders!![j].bodies.add(body)
                        } else {
                            val body = ExpandableItemBody(el[0].text(), "", el[1].html())
                            mHeaders!![j].bodies.add(body)
                        }
                    } else {
                        j++
                    }
                }
                size = rows!!.size
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (n: NullPointerException) {
                return 0
            }
            return size
        }

        override fun onPostExecute(s: Int) {
            mSwipeRefreshLayout!!.isRefreshing = false
            if (s == 0) {
                if (context != null) Toast.makeText(context, R.string.error,
                        Toast.LENGTH_LONG).show()
            } else {
                mExpandableListView!!.setAdapter(mAdapter)
                for (i in mHeaders!!.indices) {
                    mExpandableListView!!.expandGroup(i)
                }
            }
        }
    }
}