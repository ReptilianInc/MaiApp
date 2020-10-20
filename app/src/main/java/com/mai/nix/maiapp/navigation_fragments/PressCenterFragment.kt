package com.mai.nix.maiapp.navigation_fragments

import androidx.viewpager.widget.ViewPager
import com.mai.nix.maiapp.NewsFragment
import com.mai.nix.maiapp.TabsFragment
import com.mai.nix.maiapp.ViewPagerAdapter

/**
 * Created by Nix on 29.07.2017.
 */
class PressCenterFragment : TabsFragment() {
    override fun setupViewPager(viewPager: ViewPager?) {
        setHasOptionsMenu(false)
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NewsFragment.newInstance(NewsFragment.NEWS_CODE), "Новости")
        adapter.addFragment(NewsFragment.newInstance(NewsFragment.EVENTS_CODE), "Мероприятия")
        adapter.addFragment(NewsFragment.newInstance(NewsFragment.ANNOUNCEMENTS_CODE), "Объявления")
        viewPager?.adapter = adapter
    }
}