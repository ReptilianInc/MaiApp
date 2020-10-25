package com.mai.nix.maiapp.navigation_fragments.exams

import androidx.viewpager2.widget.ViewPager2
import com.mai.nix.maiapp.ExamItemChooseGroupFragment
import com.mai.nix.maiapp.ExamsFragment
import com.mai.nix.maiapp.TabsFragment
import com.mai.nix.maiapp.ViewPagerAdapter

/**
 * Created by Nix on 14.08.2017.
 */
class ExamScheduleFragment : TabsFragment() {
    override fun setupViewPager(viewPager: ViewPager2) {
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFragment(ExamsFragment(), "Моё расписание")
        adapter.addFragment(ExamItemChooseGroupFragment(), "Выбрать группу")
        viewPager.adapter = adapter
    }
}