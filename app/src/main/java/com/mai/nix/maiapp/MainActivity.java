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

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private Toolbar mToolbar;
    public boolean subjectsNeedToUpdate = false;
    public boolean examsNeedToUpdate = false;
    private int mSelectedItemId = -999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.kek);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.menu_schedule);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.center_view);
        mFragment = new ScheduleFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.center_view, mFragment)
                .commit();
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.menu_schedule,
                R.string.menu_schedule) {

            @Override
            public void onDrawerClosed(View drawerView) {
                switch (mSelectedItemId) {
                    case R.id.menu_sch:
                        setFragment("Расписание пар", new ScheduleFragment());
                        break;
                    case R.id.menu_sch_ex:
                        setFragment("Расписание сессии", new ExamScheduleFragment());
                        break;
                    case R.id.press:
                        setFragment("Пресс-центр", new PressCenterFragment());
                        break;
                    case R.id.menu_campus:
                        setFragment("Кампус", new CampusFragment());
                        break;
                    case R.id.life:
                        setFragment("Жизнь", new LifeFragment());
                        break;
                    case R.id.menu_settings:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(intent, UPDATE_SCHEDULE);
                        break;
                }
                mSelectedItemId = -999;
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationview = findViewById(R.id.navigation);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                mSelectedItemId = item.getItemId();
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setFragment(String title, Fragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(mFragment)
                .commit();
        mToolbar.setTitle(title);
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
