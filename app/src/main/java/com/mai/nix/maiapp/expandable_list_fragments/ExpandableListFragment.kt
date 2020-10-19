package com.mai.nix.maiapp.expandable_list_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.nix.maiapp.R
import kotlinx.android.synthetic.main.fragment_expandable_list.*

/**
 * Created by Nix on 13.09.2017.
 */
abstract class ExpandableListFragment : Fragment() {
    protected abstract fun releaseThread()

    protected var adapter = ExpandableListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expandable_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expandableListSwipeRefreshLayout.isRefreshing = true
        releaseThread()
        prepareRecyclerView()
        expandableListSwipeRefreshLayout.setOnRefreshListener {
            expandableListSwipeRefreshLayout.isRefreshing = true
            releaseThread()
        }
    }

    private fun prepareRecyclerView() {
        expandableRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        expandableRecyclerView.adapter = adapter
    }
}