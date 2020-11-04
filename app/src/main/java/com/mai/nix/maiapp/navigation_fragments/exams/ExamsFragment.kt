package com.mai.nix.maiapp.navigation_fragments.exams

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.nix.maiapp.MVIEntity
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.helpers.Parser
import com.mai.nix.maiapp.helpers.UserSettings
import kotlinx.android.synthetic.main.fragment_exams_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Nix on 01.08.2017.
 */

@ExperimentalCoroutinesApi
class ExamsFragment : Fragment(), MVIEntity {

    private lateinit var examsViewModel: ExamsViewModel

    private val examAdapter = ExamsAdapter()

    private var selectedGroup = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exams_layout, container, false)

        //val mAdapter = ExamAdapter(context, mExamModels)
        /*if (!((MainActivity) getActivity()).examsNeedToUpdate) {
            if (mDataLab.isExamsTableEmpty()) {
                new MyThread(true).execute();
            } else if (UserSettings.getExamsUpdateFrequency(getContext()).equals(UserSettings.EVERY_DAY) &&
                    UserSettings.getDay(getContext()) != mCurrentDay) {
                new MyThread(true).execute();
            }
            if (UserSettings.getExamsUpdateFrequency(getContext()).equals(UserSettings.EVERY_WEEK) &&
                    UserSettings.getWeek(getContext()) != mCurrentWeek) {
                new MyThread(true).execute();
            } else {
                mExamModels.addAll(mDataLab.getExams());
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }*/
        //examsListView.adapter = mAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mCalendar = GregorianCalendar()
        val mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        val mCurrentWeek = mCalendar.get(Calendar.WEEK_OF_MONTH)
        selectedGroup = UserSettings.getGroup(requireContext())?: ""
        //val mCurrentLink = mLink + mCurrentGroup
        prepareRecyclerView()
        examsSwipeRefreshLayout.setOnRefreshListener {
            update()
        }
        setupViewModel()
        observeViewModel()
        load()
    }

    private fun load() {
        lifecycleScope.launch {
            examsViewModel.examsIntent.send(ExamsIntent.LoadExams(selectedGroup, useDb = true))
        }
    }

    private fun update() {
        lifecycleScope.launch {
            examsViewModel.examsIntent.send(ExamsIntent.UpdateExams(selectedGroup, updateDb = true))
        }
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            examsViewModel.state.collect {
                examsSwipeRefreshLayout.isRefreshing = it.loading
                examAdapter.updateItems(it.exams)
                examAdapter.notifyDataSetChanged()
                if (!it.error.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun setupViewModel() {
        examsViewModel = ViewModelProviders.of(this, ExamsViewModelFactory(requireContext().applicationContext as Application)).get(ExamsViewModel::class.java)
    }

    private fun prepareRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        examsRecyclerView.layoutManager = linearLayoutManager
        examsRecyclerView.adapter = examAdapter
        val dividerItemDecoration = DividerItemDecoration(requireContext(), linearLayoutManager.orientation)
        examsRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.action_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_button) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, Parser.generateExamsLink(selectedGroup))
            i.putExtra(Intent.EXTRA_SUBJECT, selectedGroup)
            startActivity(Intent.createChooser(i, getString(R.string.share_exam_link)))
        } else if (item.itemId == R.id.browser_button) {
            val builder = CustomTabsIntent.Builder()
            builder.setShowTitle(true)
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(Parser.generateExamsLink(selectedGroup)))
        }
        return super.onOptionsItemSelected(item)
    }
}