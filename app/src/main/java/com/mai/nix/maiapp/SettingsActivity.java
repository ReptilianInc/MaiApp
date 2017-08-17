package com.mai.nix.maiapp;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.mai.nix.maiapp.navigation_fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.add(R.id.f_container, settingsFragment);
        fragmentTransaction.commit();
    }
}
