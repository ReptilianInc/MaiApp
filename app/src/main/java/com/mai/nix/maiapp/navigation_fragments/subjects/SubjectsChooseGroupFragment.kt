package com.mai.nix.maiapp.navigation_fragments.subjects

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.nix.maiapp.ActivityChooseSingleItem
import com.mai.nix.maiapp.MVIEntity
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.choose_groups.NewChooseGroupActivity
import com.mai.nix.maiapp.helpers.Parser
import kotlinx.android.synthetic.main.fragment_subjects_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Nix on 01.08.2017.
 */

@ExperimentalCoroutinesApi
class SubjectsChooseGroupFragment : Fragment(), MVIEntity {

    companion object {
        const val CHOOSE_WEEK_RESULT_CODE = 567
        const val CHOOSE_GROUP_RESULT_CODE = 568
    }

    private lateinit var subjectsViewModel: SubjectsViewModel

    private val adapter = SubjectsAdapter()

    private val weeks = arrayOf(
            "Текущая неделя",
            "1 неделя",
            "2 неделя",
            "3 неделя",
            "4 неделя",
            "5 неделя",
            "6 неделя",
            "7 неделя",
            "8 неделя",
            "9 неделя",
            "10 неделя",
            "11 неделя",
            "12 неделя",
            "13 неделя",
            "14 неделя",
            "15 неделя",
            "16 неделя",
            "17 неделя",
            "18 неделя",
            "19 неделя",
            "20 неделя",
            "21 неделя",
            "22 неделя"
    )

    private var selectedGroup = ""
    private var selectedWeek = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subjects_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        setupViewModel()
        observeViewModel()
        chooseGroupButton.visibility = View.VISIBLE
        chooseGroupButton.setOnClickListener {
            startActivityForResult(NewChooseGroupActivity.newIntent(requireContext(), true), CHOOSE_GROUP_RESULT_CODE)
        }
        chooseWeekButton.setOnClickListener {
            ActivityChooseSingleItem.startActivity(requireActivity() as AppCompatActivity, this, weeks, CHOOSE_WEEK_RESULT_CODE)
        }
        subjectsSwipeRefreshLayout.setOnRefreshListener {
            update()
        }
    }

    private fun update() {
        lifecycleScope.launch {
            subjectsViewModel.subjectsIntent.send(SubjectsIntent.UpdateSubjects(selectedGroup, updateDb = false))
        }
    }

    private fun prepareRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        subjectsRecyclerView.layoutManager = linearLayoutManager
        subjectsRecyclerView.adapter = adapter
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            subjectsViewModel.state.collect {
                subjectsViewModel.state.collect {
                    subjectsSwipeRefreshLayout.isRefreshing = it.loading
                    selectedGroup = it.group
                    chooseGroupButton.text = if (selectedGroup.isEmpty()) requireContext().getString(R.string.choose_group_space) else selectedGroup
                    adapter.updateItems(it.schedules)
                    adapter.notifyDataSetChanged()
                    chooseWeekButton.text = weeks[it.week]
                    selectedWeek = if (it.week > 0) it.week.toString() else ""
                    if (!it.error.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun setupViewModel() {
        subjectsViewModel = ViewModelProviders.of(this, SubjectsViewModelFactory(requireContext().applicationContext as Application)).get(SubjectsViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_WEEK_RESULT_CODE) {
                val chosenIndex = data?.getIntExtra(ActivityChooseSingleItem.ITEMS_RESULT, 0) ?: 0
                lifecycleScope.launch {
                    subjectsViewModel.subjectsIntent.send(SubjectsIntent.SetWeek(chosenIndex, useDb = false))
                }
            } else if (requestCode == CHOOSE_GROUP_RESULT_CODE) {
                val chosenGroup = data?.getStringExtra(NewChooseGroupActivity.EXTRA_GROUP)?: ""
                lifecycleScope.launch {
                    subjectsViewModel.subjectsIntent.send(SubjectsIntent.SetGroup(chosenGroup, useDb = false))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.action_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (selectedGroup.isEmpty()) {
            Toast.makeText(context, R.string.exception_group_null, Toast.LENGTH_SHORT).show()
            return super.onOptionsItemSelected(item)
        }
        if (item.itemId == R.id.share_button) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, Parser.generateSubjectsLink(selectedGroup, selectedWeek))
            i.putExtra(Intent.EXTRA_SUBJECT, selectedGroup)
            startActivity(Intent.createChooser(i, getString(R.string.share_subjects_link)))
        } else if (item.itemId == R.id.browser_button) {
            val builder = CustomTabsIntent.Builder()
            builder.setShowTitle(true)
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(Parser.generateSubjectsLink(selectedGroup, selectedWeek)))
        }
        return super.onOptionsItemSelected(item)
    }
}