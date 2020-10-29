package com.mai.nix.maiapp

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
import com.mai.nix.maiapp.helpers.UserSettings
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsAdapter
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsIntent
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsViewModel
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsViewModelFactory
import kotlinx.android.synthetic.main.fragment_exams_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Nix on 01.08.2017.
 */

@ExperimentalCoroutinesApi
class ExamsFragment : Fragment(), MVIEntity, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var examsViewModel: ExamsViewModel

    private val examAdapter = ExamsAdapter()

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
        UserSettings.registerListener(this)
        val mCalendar = GregorianCalendar()
        val mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        val mCurrentWeek = mCalendar.get(Calendar.WEEK_OF_MONTH)
        val mCurrentGroup = UserSettings.getGroup(requireContext())
        //val mCurrentLink = mLink + mCurrentGroup
        prepareRecyclerView()
        examsSwipeRefreshLayout.setOnRefreshListener {
            load()
        }
        setupViewModel()
        observeViewModel()
        load()

        examsSwipeRefreshLayout.setOnRefreshListener {
            examsSwipeRefreshLayout.isRefreshing = true
        }
    }

    override fun onDetach() {
        super.onDetach()
        UserSettings.unregisterListener(this)
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        load()
    }

    private fun load() {
        lifecycleScope.launch {
            examsViewModel.examsIntent.send(ExamsIntent.LoadExams(UserSettings.getGroup(requireContext())?: ""))
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
        examsViewModel = ViewModelProviders.of(this, ExamsViewModelFactory()).get(ExamsViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        /*if (((MainActivity) getActivity()).examsNeedToUpdate) {
            mCurrentGroup = UserSettings.getGroup(getContext());
            new MyThread(true).execute();
            ((MainActivity) getActivity()).examsNeedToUpdate = false;
        }*/
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
            //i.putExtra(Intent.EXTRA_TEXT, mCurrentLink)
            //i.putExtra(Intent.EXTRA_SUBJECT, mCurrentGroup)
            startActivity(Intent.createChooser(i, getString(R.string.share_exam_link)))
        } else if (item.itemId == R.id.browser_button) {
            val builder = CustomTabsIntent.Builder()
            builder.setShowTitle(true)
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            val customTabsIntent = builder.build()
            //customTabsIntent.launchUrl(requireContext(), Uri.parse(mCurrentLink))
        }
        return super.onOptionsItemSelected(item)
    }
}