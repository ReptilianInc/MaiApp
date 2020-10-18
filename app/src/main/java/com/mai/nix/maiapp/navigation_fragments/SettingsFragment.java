package com.mai.nix.maiapp.navigation_fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mai.nix.maiapp.ChooseGroupActivity;
import com.mai.nix.maiapp.DataLab;
import com.mai.nix.maiapp.R;
import com.mai.nix.maiapp.helpers.UserSettings;

/**
 * Created by Nix on 03.08.2017.
 */

public class SettingsFragment extends PreferenceFragment implements android.preference.Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {
    private Preference mGroupPreference;
    private Preference mClearSubjectsCache;
    private Preference mClearExamsCache;
    private ListPreference mFregSubjects;
    private ListPreference mFregExams;
    private Preference mAbout;
    private Preference mMAI;
    private static final int REQUEST_CODE_GROUP = 0;
    private String mChoosenGroup;
    private DataLab mDataLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_prefs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataLab = DataLab.get(getActivity());
        mGroupPreference = getPreferenceManager().findPreference("pref_group");
        mClearSubjectsCache = getPreferenceScreen().findPreference("clear_cache_subj");
        mClearExamsCache = getPreferenceScreen().findPreference("clear_cache_ex");
        mFregSubjects = (ListPreference) getPreferenceScreen().findPreference("freg");
        mFregExams = (ListPreference) getPreferenceScreen().findPreference("freg_ex");
        mGroupPreference.setSummary(UserSettings.getGroup(getActivity()));
        mFregSubjects.setValue(UserSettings.getSubjectsUpdateFrequency(getActivity()));
        mFregExams.setValue(UserSettings.getExamsUpdateFrequency(getActivity()));
        mAbout = getPreferenceScreen().findPreference("about");
        mMAI = getPreferenceScreen().findPreference("go_mai");
        mGroupPreference.setOnPreferenceClickListener(this);
        mClearSubjectsCache.setOnPreferenceClickListener(this);
        mClearExamsCache.setOnPreferenceClickListener(this);
        mAbout.setOnPreferenceClickListener(this);
        mMAI.setOnPreferenceClickListener(this);
        mFregSubjects.setOnPreferenceChangeListener(this);
        mFregExams.setOnPreferenceChangeListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case "pref_group":
                Intent i = ChooseGroupActivity.newIntent(getActivity(), false);
                startActivityForResult(i, REQUEST_CODE_GROUP);
                break;
            case "clear_cache_subj":
                mDataLab.clearSubjectsCache();
                Toast.makeText(getActivity(), R.string.clear_cache_subj_toast, Toast.LENGTH_SHORT).show();
                break;
            case "clear_cache_ex":
                mDataLab.clearExamsCache();
                Toast.makeText(getActivity(), R.string.clear_cache_exams_toast, Toast.LENGTH_SHORT).show();
                break;
            case "about":
                Toast.makeText(getActivity(), R.string.author, Toast.LENGTH_SHORT).show();
                break;
            case "go_mai":
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setShowTitle(true);
                builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorText));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getActivity(), Uri.parse("http://mai.ru/"));
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case "freg":
                UserSettings.setSubjectsUpdateFrequency(getActivity(), (String) newValue);
                break;
            case "freg_ex":
                UserSettings.setExamsUpdateFrequency(getActivity(), (String) newValue);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_GROUP) {
            if (data == null) {
                return;
            }
            mChoosenGroup = data.getStringExtra(ChooseGroupActivity.EXTRA_GROUP);
            UserSettings.setGroup(getActivity(), mChoosenGroup);
            mGroupPreference.setSummary(mChoosenGroup);
            getActivity().setResult(Activity.RESULT_OK);
        }
    }
}
