package com.mai.nix.maiapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nix on 27.04.2017.
 */

public class PressFragment extends Fragment {
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.press_fragment_layout, container, false);
        mFragmentManager = getFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
        mFragment = PressItemFragment.newInstance(PressItemFragment.NEWS_CODE);
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, mFragment)
                .commit();
        mBottomNavigationView = (BottomNavigationView) v.findViewById(R.id.bottom_nav);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_bottom_press_news:
                        setFragment(PressItemFragment.NEWS_CODE);
                        break;
                    case R.id.menu_bottom_press_events:
                        setFragment(PressItemFragment.EVENTS_CODE);
                        break;
                    case R.id.menu_bottom_press_ann:
                        setFragment(PressItemFragment.ANNOUNCEMENTS_CODE);
                        break;
                }
                return true;
            }
        });
        return v;
    }
    private void setFragment(byte code){
        mFragmentManager.beginTransaction()
                .remove(mFragment)
                .commit();
        mFragment = PressItemFragment.newInstance(code);
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container, mFragment)
                .commit();
    }
}
