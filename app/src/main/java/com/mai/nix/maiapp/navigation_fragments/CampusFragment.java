package com.mai.nix.maiapp.navigation_fragments;

import androidx.viewpager.widget.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;

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
        setHasOptionsMenu(false);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new BarracksFragment(), "Общежития");
        adapter.addFragment(new LibrariesFragment(), "Библиотека");
        adapter.addFragment(new CafesFragment(), "Столовые и кафе");
        adapter.addFragment(new MapFragment(), "Карта");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
