package com.mai.nix.maiapp.navigation_fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mai.nix.maiapp.DataLab
import com.mai.nix.maiapp.R
import com.mai.nix.maiapp.choose_groups.NewChooseGroupActivity
import com.mai.nix.maiapp.helpers.UserSettings
import com.mai.nix.maiapp.helpers.UserSettings.getExamsUpdateFrequency
import com.mai.nix.maiapp.helpers.UserSettings.getGroup
import com.mai.nix.maiapp.helpers.UserSettings.getSubjectsUpdateFrequency
import com.mai.nix.maiapp.helpers.UserSettings.setExamsUpdateFrequency
import com.mai.nix.maiapp.helpers.UserSettings.setGroup
import com.mai.nix.maiapp.helpers.UserSettings.setSubjectsUpdateFrequency
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Nix on 03.08.2017.
 */

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private lateinit var groupPreference: Preference
    private lateinit var chosenGroup: String
    private lateinit var dataLab: DataLab

    companion object {
        private const val REQUEST_CODE_GROUP = 0
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_prefs, rootKey)
        dataLab = DataLab.get(activity)
        groupPreference = findPreference("pref_group")!!
        val clearSubjectsCache = findPreference<Preference>("clear_cache_subj")
        val clearExamsCache = findPreference<Preference>("clear_cache_ex")
        val frequencySubjects = findPreference<androidx.preference.ListPreference>("frequency_update_subjects")
        val frequencyExams = findPreference<androidx.preference.ListPreference>("frequency_update_exams")
        val theme = findPreference<androidx.preference.ListPreference>("pref_theme")
        val about = findPreference<Preference>("about")
        val mai = findPreference<Preference>("go_mai")

        groupPreference.summary = getGroup(requireContext())
        frequencySubjects?.value = getSubjectsUpdateFrequency(requireContext())
        frequencyExams?.value = getExamsUpdateFrequency(requireContext())

        groupPreference.onPreferenceClickListener = this
        clearSubjectsCache?.onPreferenceClickListener = this
        clearExamsCache?.onPreferenceClickListener = this
        about?.onPreferenceClickListener = this
        mai?.onPreferenceClickListener = this
        frequencySubjects?.onPreferenceChangeListener = this
        frequencyExams?.onPreferenceChangeListener = this
        theme?.onPreferenceChangeListener = this
    }

    private fun convertThemeValue(value: Any): Int {
        val i = value as String
        return i.toInt()
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_GROUP && data != null) {
            chosenGroup = data.getStringExtra(NewChooseGroupActivity.EXTRA_GROUP)
                    ?: throw Exception("No group found")
            setGroup(requireContext(), chosenGroup)
            groupPreference.summary = chosenGroup
            requireActivity().setResult(Activity.RESULT_OK)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            "pref_group" -> {
                val i = NewChooseGroupActivity.newIntent(requireContext(), false)
                startActivityForResult(i, REQUEST_CODE_GROUP)
            }
            "clear_cache_subj" -> {
                dataLab.clearSubjectsCache()
                Toast.makeText(activity, R.string.clear_cache_subj_toast, Toast.LENGTH_SHORT).show()
            }
            "clear_cache_ex" -> {
                dataLab.clearExamsCache()
                Toast.makeText(activity, R.string.clear_cache_exams_toast, Toast.LENGTH_SHORT).show()
            }
            "about" -> Toast.makeText(activity, R.string.author, Toast.LENGTH_SHORT).show()
            "go_mai" -> {
                val builder = CustomTabsIntent.Builder()
                builder.setShowTitle(true)
                builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.colorText))
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(requireContext(), Uri.parse("http://mai.ru/"))
            }
        }
        return true
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        when (preference.key) {
            "frequency_update_subjects" -> setSubjectsUpdateFrequency(requireContext(), newValue as String)
            "frequency_update_exams" -> setExamsUpdateFrequency(requireContext(), newValue as String)
            "pref_theme" -> {
                val value = convertThemeValue(newValue)
                UserSettings.setTheme(requireContext(), value)
                AppCompatDelegate.setDefaultNightMode(value)
            }
        }
        return true
    }
}