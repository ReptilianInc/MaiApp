package com.mai.nix.maiapp.choose_groups

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mai.nix.maiapp.Parser
import com.mai.nix.maiapp.R
import kotlinx.android.synthetic.main.activity_new_choose_group.*


class NewChooseGroupActivity : AppCompatActivity(), GroupsParsingCallback, GroupsAdapter.GroupChosenListener {

    companion object {
        const val EXTRA_GROUP = "com.mai.nix.maiapp.choose_groups.group_result"
        private const val MODE = "com.mai.nix.maiapp.choose_groups.maiapp.mode"
    }

    private val faculties = arrayOf(
            "Институт №1",
            "Институт №2",
            "Институт №3",
            "Институт №4",
            "Институт №5",
            "Институт №6",
            "Институт №7",
            "Институт №8",
            "Институт №9",
            "Институт №10",
            "Институт №11",
            "Институт №12"
    )

    private var currentGroup = ""
    private val groupsAdapter = GroupsAdapter()
    private var isForSettings = false

    fun newIntent(context: Context, mode: Boolean): Intent {
        val intent = Intent(context, NewChooseGroupActivity::class.java)
        intent.putExtra(MODE, mode)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_choose_group)
        groupsAdapter.callback = this
        isForSettings = intent.getBooleanExtra(MODE, false)
        prepareRecyclerView()
        GroupsParsingThread(this).execute()
        chooseGroupSRL.setOnRefreshListener {
            GroupsParsingThread(this).execute()
        }
    }

    override fun onGroupChosen(group: String) {
        currentGroup = group
        readyButton.visibility = if (group.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun startLoad() {
        chooseGroupSRL.isRefreshing = true
    }

    override fun endLoad(groups: List<String>) {
        chooseGroupSRL.isRefreshing = false
        groupsAdapter.groups.clear()
        groupsAdapter.groups.addAll(groups)
        groupsAdapter.notifyDataSetChanged()
    }

    private fun prepareRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        groupsRecyclerView.layoutManager = linearLayoutManager
        groupsRecyclerView.adapter = groupsAdapter
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
        groupsRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private class GroupsParsingThread(val callback: GroupsParsingCallback): AsyncTask<List<String>, Void, List<String>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            callback.startLoad()
        }

        override fun doInBackground(vararg params: List<String>): List<String> {
            return Parser.parseGroups("Институт №1", 1)
        }

        override fun onPostExecute(result: List<String>) {
            super.onPostExecute(result)
            callback.endLoad(result)
        }
    }
}