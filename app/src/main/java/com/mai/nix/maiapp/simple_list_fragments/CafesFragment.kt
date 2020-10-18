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
 * Created by Nix on 14.08.2017.
 */
class CafesFragment : SimpleListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<List<SimpleListModel>, Void?, List<SimpleListModel>>() {
        private var doc: Document? = null
        private var table: Element? = null
        private var rows: Elements? = null

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
                doc = Jsoup.connect("http://mai.ru/common/campus/cafeteria/").get()
                table = doc?.select("table[class = table table-bordered]")?.first()
                rows = table?.select("tr")
                if (table == null) return list
                for (i in 1 until rows!!.size) {
                    val el = rows!!.get(i).select("td")
                    list.add(SimpleListModel(el[1].text(), el[2].text(), null, el[3].text()))
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