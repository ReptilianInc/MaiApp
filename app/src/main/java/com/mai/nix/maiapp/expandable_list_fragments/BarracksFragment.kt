package com.mai.nix.maiapp.expandable_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.ExpandableItemBody
import com.mai.nix.maiapp.model.ExpandableItemHeader
import kotlinx.android.synthetic.main.fragment_expandable_list.*
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Created by Nix on 13.08.2017.
 */
class BarracksFragment : ExpandableListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<Int?, Void?, List<ExpandableItemHeader>>() {
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

        override fun doInBackground(vararg p0: Int?): List<ExpandableItemHeader> {
            val collection = mutableListOf<ExpandableItemHeader>()
            try {
                val doc = Jsoup.connect("http://mai.ru/common/campus/dormitory.php").get()
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
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (n: NullPointerException) {
                return collection
            }
            return collection
        }
    }
}