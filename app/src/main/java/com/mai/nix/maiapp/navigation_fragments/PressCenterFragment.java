package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;
import com.mai.nix.maiapp.NewsFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 29.07.2017.
 */

public class PressCenterFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        setHasOptionsMenu(false);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(NewsFragment.newInstance(NewsFragment.NEWS_CODE), "Новости");
        adapter.addFragment(NewsFragment.newInstance(NewsFragment.EVENTS_CODE), "Мероприятия");
        adapter.addFragment(NewsFragment.newInstance(NewsFragment.ANNOUNCEMENTS_CODE), "Объявления");
        viewPager.setAdapter(adapter);
    }
}
