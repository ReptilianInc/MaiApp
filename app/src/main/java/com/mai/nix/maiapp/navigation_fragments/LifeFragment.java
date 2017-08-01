package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.TestFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 01.08.2017.
 */

public class LifeFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TestFragment(), "Студенческие объединения");
        adapter.addFragment(new TestFragment(), "Спортивные секции");
        adapter.addFragment(new TestFragment(), "Объединения сотрудников и выпускников");
        viewPager.setAdapter(adapter);
    }
}
