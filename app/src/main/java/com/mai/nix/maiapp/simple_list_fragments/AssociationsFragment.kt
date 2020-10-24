package com.mai.nix.maiapp.simple_list_fragments

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.navigation_fragments.life.AssociationsIntent
import com.mai.nix.maiapp.navigation_fragments.life.LifeAbstractFragment
import kotlinx.android.synthetic.main.fragment_abstract_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Nix on 11.08.2017.
 */

@ExperimentalCoroutinesApi
class AssociationsFragment : LifeAbstractFragment() {

    override fun initAdapter(): RecyclerView.Adapter<*> {
        return SimpleListAdapter()
    }

    override fun refresh() {
        lifecycleScope.launch {
            lifeViewModel.associationsIntent.send(AssociationsIntent.LoadAssociations)
        }
    }

    override fun setupViewModel() {
        lifecycleScope.launch {
            lifeViewModel.associationsState.collect {
                abstractListSwipeRefreshLayout.isRefreshing = it.loading
                (adapter as SimpleListAdapter).simpleListModels.addAll(it.items)
                adapter.notifyDataSetChanged()
                if (!it.error.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}