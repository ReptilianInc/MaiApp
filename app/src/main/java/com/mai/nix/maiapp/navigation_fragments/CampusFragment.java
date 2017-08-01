package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;
import com.mai.nix.maiapp.MapFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.TestFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 13.05.2017.
 */

public class CampusFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MapFragment(), "Карта");
        adapter.addFragment(new TestFragment(), "Общежития");
        adapter.addFragment(new TestFragment(), "Библиотека");
        adapter.addFragment(new TestFragment(), "Столовые и кафе");
        adapter.addFragment(new TestFragment(), "Спортивная инфраструктура");
        viewPager.setAdapter(adapter);
    }
}
