package com.mai.nix.maiapp.navigation_fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.mai.nix.maiapp.R;

/**
 * Created by Nix on 03.08.2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_test);
    }

}
