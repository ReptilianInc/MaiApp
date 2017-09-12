package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;

import com.mai.nix.maiapp.ChooseGroupScheduleFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.MyGroupScheduleSubjectsFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 01.08.2017.
 */

public class ScheduleFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyGroupScheduleSubjectsFragment(), "Моё расписание");
        adapter.addFragment(new ChooseGroupScheduleFragment(), "Выбрать группу");
        viewPager.setAdapter(adapter);
    }
}
