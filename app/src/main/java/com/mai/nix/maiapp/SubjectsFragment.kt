package com.mai.nix.maiapp

import android.content.Intent
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
import com.mai.nix.maiapp.navigation_fragments.subjects.SubjectsAdapter
import com.mai.nix.maiapp.navigation_fragments.subjects.SubjectsIntent
import com.mai.nix.maiapp.navigation_fragments.subjects.SubjectsViewModel
import com.mai.nix.maiapp.navigation_fragments.subjects.SubjectsViewModelFactory
import kotlinx.android.synthetic.main.fragment_subjects_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Nix on 02.08.2017.
 */

@ExperimentalCoroutinesApi
class SubjectsFragment : Fragment(), MVIEntity {

    private lateinit var subjectsViewModel: SubjectsViewModel

    private val adapter = SubjectsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subjects_layout, container, false)
        /*if (!((MainActivity) getActivity()).subjectsNeedToUpdate) {
            if (i != 0) {
                mWeek = Integer.toString(i);
                mCurrentLink = mLink.concat(mCurrentGroup).concat(PLUS_WEEK).concat(mWeek);
                new MyThread(mCurrentLink, false).execute();
            } else if (mDataLab.isSubjectsTablesEmpty()) {
                mCurrentLink = mLink.concat(mCurrentGroup);
                new MyThread(mCurrentLink, true).execute();
            } else if (UserSettings.getSubjectsUpdateFrequency(getContext()).equals(UserSettings.EVERY_DAY) &&
                    UserSettings.getDay(getContext()) != mCurrentDay) {
                UserSettings.setDay(getContext(), mCurrentDay);
                mCurrentLink = mLink.concat(mCurrentGroup);
                new MyThread(mCurrentLink, true).execute();
            } else if (UserSettings.getSubjectsUpdateFrequency(getContext()).equals(UserSettings.EVERY_WEEK) &&
                    UserSettings.getWeek(getContext()) != mCurrentWeek) {
                UserSettings.setWeek(getContext(), mCurrentWeek);
                mCurrentLink = mLink.concat(mCurrentGroup);
                new MyThread(mCurrentLink, true).execute();
            } else {
                mGroups.clear();
                mCurrentLink = mLink.concat(mCurrentGroup);
                ArrayList<SubjectHeader> headers = new ArrayList<>();
                headers.addAll(mDataLab.getHeaders());
                for (SubjectHeader header : headers) {
                    header.setChildren(mDataLab.getBodies(header.getUuid()));
                }
                mGroups.addAll(headers);
                for (int j = 0; j < mGroups.size(); j++) {
                    mListView.expandGroup(j);
                }
                mAdapter.notifyDataSetChanged();
            }
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mCalendar = GregorianCalendar()
        val mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        val mCurrentWeek = mCalendar.get(Calendar.WEEK_OF_MONTH)
        prepareRecyclerView()
        setupViewModel()
        observeViewModel()
        chooseWeekButton.setOnClickListener {
            //ActivityChooseSingleItem.startActivity(this, courses, NewChooseGroupActivity.COURSES_RESULT_CODE)
        }
        subjectsSwipeRefreshLayout.setOnRefreshListener {
            /*if (mSpinner.getSelectedItemPosition() != 0) {
                mWeek = Integer.toString(mSpinner.getSelectedItemPosition())
                mCurrentLink = mLink + mCurrentGroup + PLUS_WEEK + mWeek
                MyThread(mCurrentLink, false).execute()
            } else {
                mCurrentLink = mLink + mCurrentGroup
                MyThread(mCurrentLink, true).execute()
            }*/
            load()
        }
        load()
    }


    override fun observeViewModel() {
        lifecycleScope.launch {
            subjectsViewModel.state.collect {
                subjectsSwipeRefreshLayout.isRefreshing = it.loading
                adapter.updateItems(it.subjects)
                adapter.notifyDataSetChanged()
                chooseWeekButton.text = if (it.week.isEmpty()) requireActivity().getString(R.string.choose_week_space) else it.week
                if (!it.error.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun setupViewModel() {
        subjectsViewModel = ViewModelProviders.of(this, SubjectsViewModelFactory()).get(SubjectsViewModel::class.java)
    }

    private fun load() {
        lifecycleScope.launch {
            subjectsViewModel.subjectsIntent.send(SubjectsIntent.LoadSubjects(UserSettings.getGroup(requireContext())!!, ""))
        }
    }

    override fun onResume() {
        super.onResume()
        /*if (((MainActivity) getActivity()).subjectsNeedToUpdate) {
            mCurrentGroup = UserSettings.getGroup(getContext());
            mCurrentLink = mLink.concat(mCurrentGroup);
            new MyThread(mCurrentLink, true).execute();
            ((MainActivity) getActivity()).subjectsNeedToUpdate = false;
        }*/
    }

    private fun prepareRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        subjectsRecyclerView.layoutManager = linearLayoutManager
        subjectsRecyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(requireContext(), linearLayoutManager.orientation)
        subjectsRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_button) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            //i.putExtra(Intent.EXTRA_TEXT, mCurrentLink)
            //i.putExtra(Intent.EXTRA_SUBJECT, mCurrentGroup)
            startActivity(Intent.createChooser(i, getString(R.string.share_subjects_link)))
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