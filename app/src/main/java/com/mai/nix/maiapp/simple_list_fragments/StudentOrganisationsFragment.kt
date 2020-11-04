package com.mai.nix.maiapp.simple_list_fragments

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.navigation_fragments.life.LifeAbstractFragment
import com.mai.nix.maiapp.navigation_fragments.life.StudentOrganisationsIntent
import kotlinx.android.synthetic.main.fragment_abstract_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Nix on 10.08.2017.
 */

@ExperimentalCoroutinesApi
class StudentOrganisationsFragment : LifeAbstractFragment() {
    override fun initAdapter(): RecyclerView.Adapter<*> {
        return SimpleListAdapter(arrayOf(R.drawable.ic_round_author_24, R.drawable.ic_round_place_24, R.drawable.ic_round_contact_support_24))
    }

    override fun refresh() {
        lifecycleScope.launch {
            lifeViewModel.studentOrganisationsIntent.send(StudentOrganisationsIntent.LoadOrganisations)
        }
    }

    override fun setupViewModel() {
        lifecycleScope.launch {
            lifeViewModel.studentOrganisationsState.collect {
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