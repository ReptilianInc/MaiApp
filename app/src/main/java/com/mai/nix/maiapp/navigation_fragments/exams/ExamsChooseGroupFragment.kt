package com.mai.nix.maiapp.navigation_fragments.exams

import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.app.Activity
import android.app.Application
import android.net.Uri
import android.view.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.nix.maiapp.MVIEntity
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.choose_groups.NewChooseGroupActivity
import com.mai.nix.maiapp.helpers.Parser
import kotlinx.android.synthetic.main.fragment_exams_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Nix on 17.08.2017.
 */

@ExperimentalCoroutinesApi
class ExamsChooseGroupFragment : Fragment(), MVIEntity {

    companion object {
        private const val REQUEST_CODE_GROUP = 569
    }

    private lateinit var examsViewModel: ExamsViewModel

    private val examAdapter = ExamsAdapter()

    private var selectedGroup: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exams_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomDataCard.visibility = View.VISIBLE
        prepareRecyclerView()
        setupViewModel()
        observeViewModel()

        chooseGroupButton.setOnClickListener {
            startActivityForResult(NewChooseGroupActivity.newIntent(requireContext(), true), REQUEST_CODE_GROUP)
        }

        examsSwipeRefreshLayout.setOnRefreshListener {
            if (selectedGroup.isNotEmpty()) update(selectedGroup) else examsSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun load(group: String) {
        lifecycleScope.launch {
            examsViewModel.examsIntent.send(ExamsIntent.LoadExams(group, useDb = false))
        }
    }

    private fun update(group: String) {
        lifecycleScope.launch {
            examsViewModel.examsIntent.send(ExamsIntent.UpdateExams(group, updateDb = false))
        }
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            examsViewModel.state.collect {
                selectedGroup = it.group
                chooseGroupButton.text = if (selectedGroup.isEmpty()) requireContext().getString(R.string.choose_group_space) else selectedGroup
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GROUP) {
            val group = data?.getStringExtra(NewChooseGroupActivity.EXTRA_GROUP)?: ""
            load(group)
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