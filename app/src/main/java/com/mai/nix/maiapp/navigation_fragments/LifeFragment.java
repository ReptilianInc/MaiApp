package com.mai.nix.maiapp.navigation_fragments;

import android.support.v4.view.ViewPager;

import com.mai.nix.maiapp.SportSectionsFragment;
import com.mai.nix.maiapp.StudentOrgsFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.TestFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;
import com.mai.nix.maiapp.WorkersAndGradsOrgsFragment;

/**
 * Created by Nix on 01.08.2017.
 */

public class LifeFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        setHasOptionsMenu(false);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new StudentOrgsFragment(), "Студенческие объединения");
        adapter.addFragment(new SportSectionsFragment(), "Спортивные секции");
        adapter.addFragment(new WorkersAndGradsOrgsFragment(), "Объединения сотрудников и выпускников");
        viewPager.setAdapter(adapter);
    }
}
