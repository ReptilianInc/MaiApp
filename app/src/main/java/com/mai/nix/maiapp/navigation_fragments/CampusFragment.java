package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;
import com.mai.nix.maiapp.BarracksFragment;
import com.mai.nix.maiapp.CafesFragment;
import com.mai.nix.maiapp.LibrariesFragment;
import com.mai.nix.maiapp.MapFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;

/**
 * Created by Nix on 13.05.2017.
 */

public class CampusFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new BarracksFragment(), "Общежития");
        adapter.addFragment(new LibrariesFragment(), "Библиотека");
        adapter.addFragment(new CafesFragment(), "Столовые и кафе");
        adapter.addFragment(new MapFragment(), "Карта");
        viewPager.setAdapter(adapter);
    }
}
