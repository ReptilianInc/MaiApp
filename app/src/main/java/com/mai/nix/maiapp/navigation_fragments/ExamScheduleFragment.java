package com.mai.nix.maiapp.navigation_fragments;

import androidx.viewpager.widget.ViewPager;
import com.mai.nix.maiapp.ExamItemChooseGroupFragment;
import com.mai.nix.maiapp.ExamItemFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 14.08.2017.
 */

public class ExamScheduleFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ExamItemFragment(), "Моё расписание");
        adapter.addFragment(new ExamItemChooseGroupFragment(), "Выбрать группу");
        viewPager.setAdapter(adapter);
    }
}
