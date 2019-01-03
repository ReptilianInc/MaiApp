package com.mai.nix.maiapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mai.nix.maiapp.navigation_fragments.CampusFragment;
import com.mai.nix.maiapp.navigation_fragments.ExamScheduleFragment;
import com.mai.nix.maiapp.navigation_fragments.LifeFragment;
import com.mai.nix.maiapp.navigation_fragments.PressCenterFragment;
import com.mai.nix.maiapp.navigation_fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_SCHEDULE = 69;

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    public boolean subjectsNeedToUpdate = false;
    public boolean examsNeedToUpdate = false;
    private int mSelectedItemId = -999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.center_view);
        mFragment = new ScheduleFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.center_view, mFragment)
                .commit();
    }

    private void setFragment(String title, Fragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(mFragment)
                .commit();
        mFragment = fragment;
        mFragmentManager.beginTransaction()
                .add(R.id.center_view, mFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == MainActivity.UPDATE_SCHEDULE) {
            subjectsNeedToUpdate = true;
            examsNeedToUpdate = true;
        }
    }
}
