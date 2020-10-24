package com.mai.nix.maiapp.simple_list_fragments

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.navigation_fragments.campus.CafesIntent
import com.mai.nix.maiapp.navigation_fragments.campus.CampusAbstractFragment
import kotlinx.android.synthetic.main.fragment_abstract_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Nix on 14.08.2017.
 */

@ExperimentalCoroutinesApi
class CafesFragment : CampusAbstractFragment() {

    override fun initAdapter(): RecyclerView.Adapter<*> {
        return SimpleListAdapter()
    }

    override fun refresh() {
        lifecycleScope.launch {
            campusViewModel.cafesIntent.send(CafesIntent.CafesLoad)
        }
    }

    override fun setupViewModel() {
        lifecycleScope.launch {
            campusViewModel.cafesState.collect {
                abstractListSwipeRefreshLayout.isRefreshing = it.loading
                (adapter as SimpleListAdapter).updateItems(it.items)
                adapter.notifyDataSetChanged()
                if (!it.error.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}