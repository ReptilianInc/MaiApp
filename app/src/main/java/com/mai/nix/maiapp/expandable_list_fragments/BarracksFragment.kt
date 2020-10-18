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
class BarracksFragment : ExpandableListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<Int?, Void?, Int>() {
        private var doc: Document? = null
        private var table: Element? = null
        private var stupid_header: Element? = null
        private var header: Element? = null
        private var rows: Elements? = null
        override fun onPostExecute(integer: Int) {
            mSwipeRefreshLayout!!.isRefreshing = false
            if (integer == 0) {
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
                doc = Jsoup.connect("http://mai.ru/common/campus/dormitory.php").get()
                table = doc?.select("table[class=data-table]")?.first()
                stupid_header = doc?.select("h2")?.first()
                rows = table?.select("tr")
                header = table?.select("th")?.first()
                if (table != null) mHeaders!!.clear()
                mHeaders!!.add(ExpandableItemHeader(stupid_header!!.text(), mutableListOf()))
                mHeaders!!.add(ExpandableItemHeader(header!!.text()!!, mutableListOf()))
                var j = 0
                for (i in rows!!.indices) {
                    val el = rows!!.get(i).select("td")
                    if (!el.isEmpty()) {
                        val body = ExpandableItemBody(el[0].select("b").text(),
                                el[0].ownText(),
                                el[1].text())
                        mHeaders!![j].bodies.add(body)
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
    }
}