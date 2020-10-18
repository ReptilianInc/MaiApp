package com.mai.nix.maiapp.expandable_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.ExpandableItemBody
import com.mai.nix.maiapp.model.ExpandableItemHeader
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

/**
 * Created by Nix on 13.08.2017.
 */
class LibrariesFragment : ExpandableListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<Int?, Void?, Int>() {
        private var doc: Document? = null
        private var table: Element? = null
        private var rows: Elements? = null
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

        override fun doInBackground(vararg p0: Int?): Int {
            var size = 0
            try {
                doc = Jsoup.connect("http://mai.ru/common/campus/library/").get()
                table = doc?.select("table[class = table table-bordered table-hover]")?.first()
                rows = doc?.select("tr")
                if (table != null) mHeaders!!.clear()
                for (i in rows!!.indices) {
                    if (rows!!.get(i).select("th").size == 1) {
                        mHeaders!!.add(ExpandableItemHeader(rows!!.get(i).text(), mutableListOf()))
                    }
                }
                var j = 0
                for (i in 2 until rows!!.size) {
                    val el = rows!!.get(i).select("td")
                    if (!el.isEmpty()) {
                        val body = ExpandableItemBody(
                                el[0].text(),
                                el[1].html(),
                                null)
                        mHeaders!![j].bodies.add(body)
                    } else if (rows!!.get(i).select("th").size == 1) {
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
    }
}