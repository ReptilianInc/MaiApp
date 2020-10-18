package com.mai.nix.maiapp.simple_list_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mai.nix.maiapp.R
import kotlinx.android.synthetic.main.fragment_simple_list.*

/**
 * Created by Nix on 13.09.2017.
 */
abstract class SimpleListFragment : Fragment() {
    abstract fun releaseThread()

    protected var adapter = SimpleListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        simpleListSwipeRefreshLayout.isRefreshing = true
        prepareRecyclerView()
        releaseThread()
        simpleListSwipeRefreshLayout.setOnRefreshListener {
            simpleListSwipeRefreshLayout.isRefreshing = true
            releaseThread()
        }
    }

    private fun prepareRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        simpleListView.layoutManager = LinearLayoutManager(requireContext())
        simpleListView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(requireContext(), linearLayoutManager.orientation)
        simpleListView.addItemDecoration(dividerItemDecoration)
    }
}