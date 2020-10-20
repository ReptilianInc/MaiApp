package com.mai.nix.maiapp.navigation_fragments

import androidx.viewpager.widget.ViewPager
import com.mai.nix.maiapp.ChooseGroupScheduleFragment
import com.mai.nix.maiapp.MyGroupScheduleSubjectsFragment
import com.mai.nix.maiapp.TabsFragment
import com.mai.nix.maiapp.ViewPagerAdapter

/**
 * Created by Nix on 01.08.2017.
 */
class ScheduleFragment : TabsFragment() {
    override fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(MyGroupScheduleSubjectsFragment(), "Моё расписание")
        adapter.addFragment(ChooseGroupScheduleFragment(), "Выбрать группу")
        viewPager?.adapter = adapter
    }
}