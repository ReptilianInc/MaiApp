package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;
import com.mai.nix.maiapp.NewsItemFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 29.07.2017.
 */

public class PressCenterFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(NewsItemFragment.newInstance(NewsItemFragment.NEWS_CODE), "Новости");
        adapter.addFragment(NewsItemFragment.newInstance(NewsItemFragment.EVENTS_CODE), "Мероприятия");
        adapter.addFragment(NewsItemFragment.newInstance(NewsItemFragment.ANNOUNCEMENTS_CODE), "Объявления");
        viewPager.setAdapter(adapter);
    }
}
