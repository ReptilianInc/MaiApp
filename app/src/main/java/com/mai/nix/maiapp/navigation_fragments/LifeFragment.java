package com.mai.nix.maiapp.navigation_fragments;

import androidx.viewpager.widget.ViewPager;

import com.mai.nix.maiapp.SportSectionsFragment;
import com.mai.nix.maiapp.simple_list_fragments.StudentOrganisationsFragment;
import com.mai.nix.maiapp.TabsFragment;
import com.mai.nix.maiapp.ViewPagerAdapter;
import com.mai.nix.maiapp.simple_list_fragments.AssociationsFragment;

/**
 * Created by Nix on 01.08.2017.
 */

public class LifeFragment extends TabsFragment {
    @Override
    protected void setupViewPager(ViewPager viewPager) {
        setHasOptionsMenu(false);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new StudentOrganisationsFragment(), "Студенческие объединения");
        adapter.addFragment(new SportSectionsFragment(), "Спортивные секции");
        adapter.addFragment(new AssociationsFragment(), "Объединения сотрудников и выпускников");
        viewPager.setAdapter(adapter);
    }
}
