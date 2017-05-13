package com.mai.nix.maiapp;

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
        mToolbar.setTitle(R.string.app_name);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.center_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name,
                R.string.app_name);
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
                            case R.id.press:
                                mToolbar.setTitle("Пресс-центр");
                                mFragment = new PressFragment();
                                mFragmentManager.beginTransaction()
                                        .add(R.id.center_view, mFragment)
                                        .commit();
                                break;
                            case R.id.menu_campus:
                                mToolbar.setTitle("Кампус");
                                mFragment = new CampusFragment();
                                mFragmentManager.beginTransaction()
                                        .add(R.id.center_view, mFragment)
                                        .commit();
                                break;
                            default:
                                mFragmentManager.beginTransaction()
                                        .detach(mFragment)
                                        .commit();
                        }

                    }
                }, 250);
                return true;
            }
        });
    }
}
