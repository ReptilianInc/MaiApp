package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.TestFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 01.08.2017.
 */

public class ScheduleFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TestFragment(), "Текущая неделя");
        adapter.addFragment(new TestFragment(), "Выбор недели");
        adapter.addFragment(new TestFragment(), "Сессия");
        viewPager.setAdapter(adapter);
    }
}
