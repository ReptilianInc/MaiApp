package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;
import com.mai.nix.maiapp.PressItemFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 29.07.2017.
 */

public class PressCenterFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(PressItemFragment.newInstance(PressItemFragment.NEWS_CODE), "Новости");
        adapter.addFragment(PressItemFragment.newInstance(PressItemFragment.EVENTS_CODE), "Мероприятия");
        adapter.addFragment(PressItemFragment.newInstance(PressItemFragment.ANNOUNCEMENTS_CODE), "Объявления");
        viewPager.setAdapter(adapter);
    }
}
