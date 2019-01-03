package com.mai.nix.maiapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mai.nix.maiapp.navigation_fragments.ExamScheduleFragment;
import com.mai.nix.maiapp.navigation_fragments.ScheduleFragment;

public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_SCHEDULE = 69;

    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    public boolean subjectsNeedToUpdate = false;
    public boolean examsNeedToUpdate = false;

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.bottomNavigation);
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.center_view);
        mFragment = new ScheduleFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.center_view, mFragment)
                .commit();
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_schedule:
                        setFragment(new ScheduleFragment());
                        break;
                    case R.id.menu_exams:
                        setFragment(new ExamScheduleFragment());
                        break;
                    case R.id.menu_more:
                        setFragment(new TestFragment());
                        break;
                }
                return true;
            }
        });
    }

    private void setFragment(Fragment fragment) {
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
