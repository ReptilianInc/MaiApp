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
class LibrariesFragment : ExpandableListFragment() {
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
                val doc = Jsoup.connect("http://mai.ru/common/campus/library/").get()
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
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (n: NullPointerException) {
                return collection
            }
            return collection
        }
    }
}