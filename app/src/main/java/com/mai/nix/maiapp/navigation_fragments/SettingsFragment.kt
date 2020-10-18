package com.mai.nix.maiapp.navigation_fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.mai.nix.maiapp.ChooseGroupActivity
import com.mai.nix.maiapp.DataLab
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.helpers.UserSettings
import com.mai.nix.maiapp.helpers.UserSettings.getExamsUpdateFrequency
import com.mai.nix.maiapp.helpers.UserSettings.getGroup
import com.mai.nix.maiapp.helpers.UserSettings.getSubjectsUpdateFrequency
import com.mai.nix.maiapp.helpers.UserSettings.setExamsUpdateFrequency
import com.mai.nix.maiapp.helpers.UserSettings.setGroup
import com.mai.nix.maiapp.helpers.UserSettings.setSubjectsUpdateFrequency

/**
 * Created by Nix on 03.08.2017.
 */

// TODO: Либо убрать, либо сильно переделать, сейчас это кринж

class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private var mGroupPreference: Preference? = null
    private var mClearSubjectsCache: Preference? = null
    private var mClearExamsCache: Preference? = null
    private var mFregSubjects: ListPreference? = null
    private var mFregExams: ListPreference? = null
    private var mTheme: ListPreference? = null
    private var mAbout: Preference? = null
    private var mMAI: Preference? = null
    private var mChoosenGroup: String? = null
    private var mDataLab: DataLab? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.app_prefs)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDataLab = DataLab.get(activity)
        mGroupPreference = preferenceManager.findPreference("pref_group")
        mClearSubjectsCache = preferenceScreen.findPreference("clear_cache_subj")
        mClearExamsCache = preferenceScreen.findPreference("clear_cache_ex")
        mFregSubjects = preferenceScreen.findPreference("freg") as ListPreference
        mFregExams = preferenceScreen.findPreference("freg_ex") as ListPreference
        mTheme = preferenceScreen.findPreference("pref_theme") as ListPreference
        mGroupPreference!!.summary = getGroup(activity)
        mFregSubjects!!.value = getSubjectsUpdateFrequency(activity)
        mFregExams!!.value = getExamsUpdateFrequency(activity)
        mAbout = preferenceScreen.findPreference("about")
        mMAI = preferenceScreen.findPreference("go_mai")
        mGroupPreference!!.onPreferenceClickListener = this
        mClearSubjectsCache!!.setOnPreferenceClickListener(this)
        mClearExamsCache!!.setOnPreferenceClickListener(this)
        mAbout!!.setOnPreferenceClickListener(this)
        mMAI!!.setOnPreferenceClickListener(this)
        mFregSubjects!!.onPreferenceChangeListener = this
        mFregExams!!.onPreferenceChangeListener = this
        mTheme!!.onPreferenceChangeListener = this
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            "pref_group" -> {
                val i = ChooseGroupActivity.newIntent(activity, false)
                startActivityForResult(i, REQUEST_CODE_GROUP)
            }
            "clear_cache_subj" -> {
                mDataLab!!.clearSubjectsCache()
                Toast.makeText(activity, R.string.clear_cache_subj_toast, Toast.LENGTH_SHORT).show()
            }
            "clear_cache_ex" -> {
                mDataLab!!.clearExamsCache()
                Toast.makeText(activity, R.string.clear_cache_exams_toast, Toast.LENGTH_SHORT).show()
            }
            "about" -> Toast.makeText(activity, R.string.author, Toast.LENGTH_SHORT).show()
            "go_mai" -> {
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                builder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorText))
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(activity, Uri.parse("http://mai.ru/"))
            }
        }
        return true
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        when (preference.key) {
            "freg" -> setSubjectsUpdateFrequency(activity, newValue as String)
            "freg_ex" -> setExamsUpdateFrequency(activity, newValue as String)
            "pref_theme" -> {
                val value = convertThemeValue(newValue)
                UserSettings.setTheme(activity, value)
                AppCompatDelegate.setDefaultNightMode(value)
            }
        }
        return true
    }

    private fun convertThemeValue(value : Any): Int {
        val i = value as String
        return i.toInt()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_GROUP) {
            mChoosenGroup = data.getStringExtra(ChooseGroupActivity.EXTRA_GROUP)
            setGroup(activity, mChoosenGroup)
            mGroupPreference!!.summary = mChoosenGroup
            activity.setResult(Activity.RESULT_OK)
        }
    }

    companion object {
        private const val REQUEST_CODE_GROUP = 0
    }
}