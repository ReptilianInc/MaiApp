package com.mai.nix.maiapp.navigation_fragments

import androidx.viewpager.widget.ViewPager
import com.mai.nix.maiapp.ExamItemChooseGroupFragment
import com.mai.nix.maiapp.ExamItemFragment
import com.mai.nix.maiapp.TabsFragment
import com.mai.nix.maiapp.ViewPagerAdapter

/**
 * Created by Nix on 14.08.2017.
 */
class ExamScheduleFragment : TabsFragment() {
    override fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(ExamItemFragment(), "Моё расписание")
        adapter.addFragment(ExamItemChooseGroupFragment(), "Выбрать группу")
        viewPager?.adapter = adapter
    }
}