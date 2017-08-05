package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;

import com.mai.nix.maiapp.ChooseWeekScheduleFragment;
import com.mai.nix.maiapp.ExamItemFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.ThisWeekFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 01.08.2017.
 */

public class ScheduleFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ThisWeekFragment(), "Текущая неделя");
        adapter.addFragment(new ChooseWeekScheduleFragment(), "Выбор недели");
        adapter.addFragment(new ExamItemFragment(), "Сессия");
        viewPager.setAdapter(adapter);
    }
}
