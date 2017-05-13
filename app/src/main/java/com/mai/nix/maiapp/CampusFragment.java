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
 * Created by Nix on 13.05.2017.
 */

public class CampusFragment extends Fragment {
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.campus_fragment_layout_new, container, false);
        mFragmentManager = getFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_container_campus);
        mFragment = new MapFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.fragment_container_campus, mFragment)
                .commit();
        mBottomNavigationView = (BottomNavigationView) v.findViewById(R.id.bottom_nav_campus);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.map:
                        mFragmentManager.beginTransaction()
                                .remove(mFragment)
                                .commit();
                        mFragment = new MapFragment();
                        mFragmentManager.beginTransaction()
                                .add(R.id.fragment_container_campus, mFragment)
                                .commit();
                        break;
                    default:
                        mFragmentManager.beginTransaction()
                                .remove(mFragment)
                                .commit();
                        break;
                }
                return true;
            }
        });
        return v;
    }
}
