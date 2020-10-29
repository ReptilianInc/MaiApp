package com.mai.nix.maiapp.navigation_fragments

import androidx.viewpager2.widget.ViewPager2
import com.mai.nix.maiapp.SubjectsChooseGroupFragment
import com.mai.nix.maiapp.SubjectsFragment
import com.mai.nix.maiapp.TabsFragment
import com.mai.nix.maiapp.ViewPagerAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Nix on 01.08.2017.
 */

class ScheduleFragment : TabsFragment() {
    @ExperimentalCoroutinesApi
    override fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFragment(SubjectsFragment(), "Моё расписание")
        adapter.addFragment(SubjectsChooseGroupFragment(), "Выбрать группу")
        viewPager.adapter = adapter
    }
}