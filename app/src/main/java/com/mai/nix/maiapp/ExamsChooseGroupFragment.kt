package com.mai.nix.maiapp

import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.app.Activity
import android.net.Uri
import android.view.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.nix.maiapp.choose_groups.NewChooseGroupActivity
import com.mai.nix.maiapp.helpers.UserSettings
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsAdapter
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsIntent
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsViewModel
import com.mai.nix.maiapp.navigation_fragments.exams.ExamsViewModelFactory
import kotlinx.android.synthetic.main.fragment_exams_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Nix on 17.08.2017.
 */

@ExperimentalCoroutinesApi
class ExamsChooseGroupFragment : Fragment(), MVIEntity {

    private lateinit var examsViewModel: ExamsViewModel

    private val examAdapter = ExamsAdapter()

    private var mSelectedGroup: String? = null

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
        examsSwipeRefreshLayout.setOnRefreshListener {
            load()
        }
        setupViewModel()
        observeViewModel()

        chooseGroupButton.setOnClickListener {
            startActivityForResult(NewChooseGroupActivity.newIntent(requireContext(), true), REQUEST_CODE_GROUP)
        }

        examsSwipeRefreshLayout.setOnRefreshListener {
            load()
        }

        examsSwipeRefreshLayout.setOnRefreshListener {
            examsSwipeRefreshLayout.isRefreshing = true
        }
    }

    private fun load() {
        lifecycleScope.launch {
            examsViewModel.examsIntent.send(ExamsIntent.LoadExams(UserSettings.getGroup(requireContext())
                    ?: ""))
        }
    }

    override fun observeViewModel() {
        lifecycleScope.launch {
            examsViewModel.state.collect {
                examsSwipeRefreshLayout.isRefreshing = it.loading
                examAdapter.updateItems(it.exams)
                if (!it.error.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun setupViewModel() {
        examsViewModel = ViewModelProviders.of(this, ExamsViewModelFactory()).get(ExamsViewModel::class.java)
    }

    private fun prepareRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        examsRecyclerView.layoutManager = linearLayoutManager
        examsRecyclerView.adapter = examAdapter
        val dividerItemDecoration = DividerItemDecoration(requireContext(), linearLayoutManager.orientation)
        examsRecyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_GROUP) {
            if (data == null) {
                return
            }
            mSelectedGroup = data.getStringExtra(NewChooseGroupActivity.EXTRA_GROUP)
            chooseGroupButton.text = mSelectedGroup
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mSelectedGroup == null) {
            Toast.makeText(context, R.string.exception_group_null, Toast.LENGTH_SHORT).show()
            return super.onOptionsItemSelected(item)
        }
        if (item.itemId == R.id.share_button) {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            //i.putExtra(Intent.EXTRA_TEXT, mLink + mSelectedGroup)
            i.putExtra(Intent.EXTRA_SUBJECT, mSelectedGroup)
            startActivity(Intent.createChooser(i, getString(R.string.share_exam_link)))
        } else if (item.itemId == R.id.browser_button) {
            val builder = CustomTabsIntent.Builder()
            builder.setShowTitle(true)
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            val customTabsIntent = builder.build()
            //customTabsIntent.launchUrl(requireContext(), Uri.parse(mLink + mSelectedGroup))
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REQUEST_CODE_GROUP = 0
    }
}