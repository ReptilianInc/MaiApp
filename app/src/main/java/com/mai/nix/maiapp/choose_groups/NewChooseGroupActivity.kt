package com.mai.nix.maiapp.choose_groups

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mai.nix.maiapp.ActivityChooseGroupPreferences
import com.mai.nix.maiapp.Parser
import com.mai.nix.maiapp.R
import kotlinx.android.synthetic.main.activity_new_choose_group.*


class NewChooseGroupActivity : AppCompatActivity(), GroupsParsingCallback, GroupsAdapter.GroupChosenListener {

    companion object {
        const val FACULTIES_RESULT_CODE = 23
        const val COURSES_RESULT_CODE = 24

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

    private val courses = arrayOf(
            "1 курс",
            "2 курс",
            "3 курс",
            "4 курс",
            "5 курс",
            "6 курс"
    )

    private var currentGroup = ""
    private var currentFacultyIndex = -1
    private var currentCourseIndex = -1
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
        chooseFacultyButton.setOnClickListener {
            ActivityChooseGroupPreferences.startActivity(this, faculties, FACULTIES_RESULT_CODE)
        }
        chooseCourseButton.setOnClickListener {
            ActivityChooseGroupPreferences.startActivity(this, courses, COURSES_RESULT_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val chosenIndex = data?.getIntExtra(ActivityChooseGroupPreferences.ITEMS_RESULT, 0)?: 0
            if (requestCode == FACULTIES_RESULT_CODE) {
                currentFacultyIndex = chosenIndex
                chooseFacultyButton.text = faculties[currentFacultyIndex]
            } else if (requestCode == COURSES_RESULT_CODE) {
                currentCourseIndex = chosenIndex
                chooseCourseButton.text = courses[currentCourseIndex]
            }
        }
    }

    private fun prepareRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        groupsRecyclerView.layoutManager = linearLayoutManager
        groupsRecyclerView.adapter = groupsAdapter
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)
        groupsRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    private class GroupsParsingThread(val callback: GroupsParsingCallback) : AsyncTask<List<String>, Void, List<String>>() {

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