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
 * Created by Nix on 10.08.2017.
 */
class StudentOrganisationsFragment : SimpleListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<List<SimpleListModel>, Void, List<SimpleListModel>>() {
        private var doc: Document? = null
        private var table: Element? = null
        private var rows: Elements? = null
        private var cols: Elements? = null

        override fun onPostExecute(list: List<SimpleListModel>) {
            simpleListSwipeRefreshLayout.isRefreshing = false
            if (list.isEmpty()) {
                if (context != null) Toast.makeText(context, R.string.error,
                        Toast.LENGTH_LONG).show()
            } else {
                adapter.simpleListModels.addAll(list)
                adapter.notifyDataSetChanged()
            }
        }

        override fun doInBackground(vararg p0: List<SimpleListModel>): List<SimpleListModel> {
            val list = mutableListOf<SimpleListModel>()
            try {
                doc = Jsoup.connect("http://www.mai.ru/life/join/index.php").get()
                table = doc?.select("table[class=data-table]")?.first()
                rows = table?.select("th")
                cols = table?.select("td")
                if (table == null) return list
                var i = 0
                var j = 0
                while (j < cols!!.size) {
                    list.add(SimpleListModel(rows!!.get(i).text(), cols!!.get(j).text(), cols!!.get(j + 1).text(),
                            cols!!.get(j + 2).text()))
                    i++
                    j += 3
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return list
            } catch (n: NullPointerException) {
                return list
            }
            return list
        }
    }
}