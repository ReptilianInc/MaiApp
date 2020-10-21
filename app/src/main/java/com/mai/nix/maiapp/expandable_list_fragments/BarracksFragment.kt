package com.mai.nix.maiapp.expandable_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.helpers.Parser
import com.mai.nix.maiapp.model.ExpandableItemHeader
import kotlinx.android.synthetic.main.fragment_expandable_list.*

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
            return Parser.parseBarracks()
        }
    }
}