package com.mai.nix.maiapp.simple_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.model.StudentOrgModel
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

    private inner class MyThread : AsyncTask<Int?, Void?, Int>() {
        private var doc: Document? = null
        private var table: Element? = null
        private var rows: Elements? = null

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
                doc = Jsoup.connect("http://mai.ru/common/campus/cafeteria/").get()
                table = doc?.select("table[class = table table-bordered]")?.first()
                rows = table?.select("tr")
                if (table != null) simpleListCollection!!.clear()
                for (i in 1 until rows!!.size) {
                    val el = rows!!.get(i).select("td")
                    simpleListCollection!!.add(StudentOrgModel(el[1].text(), el[2].text(), el[3].text()))
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