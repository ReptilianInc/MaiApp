package com.mai.nix.maiapp.simple_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.SimpleListModel
import kotlinx.android.synthetic.main.student_orgs_layout.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

/**
 * Created by Nix on 11.08.2017.
 */
class WorkersAndGradsOrgsFragment : SimpleListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<Int, Void, Int>() {
        private var doc: Document? = null
        private var table: Element? = null
        private var rows: Elements? = null
        private var cols: Elements? = null

        override fun onPostExecute(s: Int) {
            simpleListSwipeRefreshLayout.isRefreshing = false
            if (s == 0) {
                if (context != null) Toast.makeText(context, R.string.error,
                        Toast.LENGTH_LONG).show()
            } else {
                simpleListView.adapter = mAdapter
            }
        }

        override fun doInBackground(vararg p0: Int?): Int {
            var size = 0
            try {
                doc = Jsoup.connect("http://www.mai.ru/life/associations/").get()
                table = doc?.select("table[class=data-table]")?.first()
                rows = table?.select("th")
                cols = table?.select("td")
                if (table != null) simpleListCollection!!.clear()
                var i = 0
                var j = 0
                while (j < cols!!.size) {
                    simpleListCollection!!.add(SimpleListModel(rows!!.get(i).text(), cols!!.get(j).text(), cols!!.get(j + 1).text(),
                            cols!!.get(j + 2).text()))
                    i++
                    j += 3
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