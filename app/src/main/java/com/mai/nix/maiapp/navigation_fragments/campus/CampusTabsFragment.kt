package com.mai.nix.maiapp.navigation_fragments.campus

import android.view.Menu
import android.view.MenuInflater
import androidx.viewpager.widget.ViewPager
import com.mai.nix.maiapp.MapFragment
import com.mai.nix.maiapp.TabsFragment
import com.mai.nix.maiapp.ViewPagerAdapter
import com.mai.nix.maiapp.expandable_list_fragments.BarracksFragment
import com.mai.nix.maiapp.expandable_list_fragments.LibrariesFragment
import com.mai.nix.maiapp.simple_list_fragments.CafesFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Nix on 13.05.2017.
 */
class CampusTabsFragment : TabsFragment() {
    @ExperimentalCoroutinesApi
    override fun setupViewPager(viewPager: ViewPager?) {
        setHasOptionsMenu(false)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(BarracksFragment(), "Общежития")
        adapter.addFragment(LibrariesFragment(), "Библиотека")
        adapter.addFragment(CafesFragment(), "Столовые и кафе")
        adapter.addFragment(MapFragment(), "Карта")
        viewPager?.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}