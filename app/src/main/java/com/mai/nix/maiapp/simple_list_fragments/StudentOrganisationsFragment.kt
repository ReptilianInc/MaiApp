package com.mai.nix.maiapp.simple_list_fragments

import android.os.AsyncTask
import android.widget.Toast
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.helpers.Parser
import com.mai.nix.maiapp.model.SimpleListModel
import kotlinx.android.synthetic.main.student_orgs_layout.*

/**
 * Created by Nix on 10.08.2017.
 */
class StudentOrganisationsFragment : SimpleListFragment() {
    override fun releaseThread() {
        MyThread().execute()
    }

    private inner class MyThread : AsyncTask<List<SimpleListModel>, Void, List<SimpleListModel>>() {

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
            return Parser.parseStudentOrganisations()
        }
    }
}