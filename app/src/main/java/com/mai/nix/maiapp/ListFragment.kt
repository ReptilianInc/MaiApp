package com.mai.nix.maiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_simple_list.*

abstract class ListFragment: Fragment(), MVIEntity {

    protected lateinit var adapter: RecyclerView.Adapter<*>

    abstract fun initAdapter(): RecyclerView.Adapter<*>

    abstract fun refresh()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = initAdapter()
        prepareRecyclerView()
        observeViewModel()
        setupViewModel()
        simpleListSwipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
        refresh()
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