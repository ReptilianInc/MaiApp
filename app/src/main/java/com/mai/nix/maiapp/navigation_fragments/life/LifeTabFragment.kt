package com.mai.nix.maiapp.navigation_fragments.life

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.viewpager2.widget.ViewPager2
import com.mai.nix.maiapp.TabsFragment
import com.mai.nix.maiapp.ViewPagerAdapter
import com.mai.nix.maiapp.expandable_list_fragments.SportSectionsFragment
import com.mai.nix.maiapp.simple_list_fragments.AssociationsFragment
import com.mai.nix.maiapp.simple_list_fragments.StudentOrganisationsFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Nix on 01.08.2017.
 */

class LifeTabFragment : TabsFragment() {
    @ExperimentalCoroutinesApi
    override fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFragment(StudentOrganisationsFragment(), "Студенческие объединения")
        adapter.addFragment(SportSectionsFragment(), "Спортивные секции")
        adapter.addFragment(AssociationsFragment(), "Объединения сотрудников и выпускников")
        viewPager.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
}