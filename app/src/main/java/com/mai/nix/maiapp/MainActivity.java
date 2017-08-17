package com.mai.nix.maiapp;

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

import com.mai.nix.maiapp.navigation_fragments.CampusFragment;
import com.mai.nix.maiapp.navigation_fragments.ExamScheduleFragment;
import com.mai.nix.maiapp.navigation_fragments.LifeFragment;
import com.mai.nix.maiapp.navigation_fragments.PressCenterFragment;
import com.mai.nix.maiapp.navigation_fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar mToolbar = (Toolbar)findViewById(R.id.kek);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.menu_schedule);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.center_view);
        mFragment = new ScheduleFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.center_view, mFragment)
                .commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.menu_schedule,
                R.string.menu_schedule);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        final NavigationView navigationview = (NavigationView) findViewById(R.id.navigation);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                mDrawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (item.getItemId()){
                            case R.id.menu_sch:
                                mFragmentManager.beginTransaction()
                                        .remove(mFragment)
                                        .commit();
                                mToolbar.setTitle("Расписание пар");
                                mFragment = new ScheduleFragment();
                                mFragmentManager.beginTransaction()
                                        .add(R.id.center_view, mFragment)
                                        .commit();
                                break;
                            case R.id.menu_sch_ex:
                                mFragmentManager.beginTransaction()
                                        .remove(mFragment)
                                        .commit();
                                mToolbar.setTitle("Расписание сессии");
                                mFragment = new ExamScheduleFragment();
                                mFragmentManager.beginTransaction()
                                        .add(R.id.center_view, mFragment)
                                        .commit();
                                break;
                            case R.id.press:
                                mFragmentManager.beginTransaction()
                                        .remove(mFragment)
                                        .commit();
                                mToolbar.setTitle("Пресс-центр");
                                mFragment = new PressCenterFragment();
                                mFragmentManager.beginTransaction()
                                        .add(R.id.center_view, mFragment)
                                        .commit();
                                break;
                            case R.id.menu_campus:
                                mFragmentManager.beginTransaction()
                                        .remove(mFragment)
                                        .commit();
                                mToolbar.setTitle("Кампус");
                                mFragment = new CampusFragment();
                                mFragmentManager.beginTransaction()
                                        .add(R.id.center_view, mFragment)
                                        .commit();
                                break;
                            case R.id.life:
                                mFragmentManager.beginTransaction()
                                        .remove(mFragment)
                                        .commit();
                                mToolbar.setTitle("Жизнь");
                                mFragment = new LifeFragment();
                                mFragmentManager.beginTransaction()
                                        .add(R.id.center_view, mFragment)
                                        .commit();
                                break;
                            case R.id.menu_settings:
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                break;
                        }

                    }
                }, 250);
                return true;
            }
        });
    }
}
