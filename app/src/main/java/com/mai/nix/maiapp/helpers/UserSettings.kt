package com.mai.nix.maiapp.helpers

import android.content.Context
import android.content.SharedPreferences
import com.mai.nix.maiapp.R

/**
 * Created by Nix on 08.09.2017.
 */
object UserSettings {
    private var sSharedPreferences: SharedPreferences? = null
    private var sEditor: SharedPreferences.Editor? = null

    const val EVERY_DAY = "1"
    const val EVERY_WEEK = "2"
    const val ONLY_FORCIBLY = "3"

    const val LIGHT = 1
    const val DARK = 2
    const val SYSTEM = -1

    private const val PREF_TITLE = "mai_app"

    @JvmStatic
    fun initialize(context: Context) {
        sSharedPreferences = context.getSharedPreferences(PREF_TITLE, Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sSharedPreferences?.registerOnSharedPreferenceChangeListener(listener)
    }

    @JvmStatic
    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sSharedPreferences?.unregisterOnSharedPreferenceChangeListener(listener)
    }

    @JvmStatic
    fun getGroup(context: Context): String? {
        return sSharedPreferences?.getString(context.getString(R.string.pref_group), null)
    }

    @JvmStatic
    fun setGroup(context: Context, group: String?) {
        sEditor = sSharedPreferences?.edit()
        sEditor?.putString(context.getString(R.string.pref_group), group)
        sEditor?.apply()
    }

    @JvmStatic
    fun getSubjectsUpdateFrequency(context: Context): String? {
        return sSharedPreferences?.getString(context.getString(R.string.freg_cache),
                ONLY_FORCIBLY)
    }

    @JvmStatic
    fun setSubjectsUpdateFrequency(context: Context, value: String?) {
        sEditor = sSharedPreferences?.edit()
        sEditor?.putString(context.getString(R.string.freg_cache), value)
        sEditor?.apply()
    }

    @JvmStatic
    fun getExamsUpdateFrequency(context: Context): String? {
        return sSharedPreferences?.getString(context.getString(R.string.freg_cache_exams),
                ONLY_FORCIBLY)
    }

    @JvmStatic
    fun setExamsUpdateFrequency(context: Context, value: String?) {
        sEditor = sSharedPreferences?.edit()
        sEditor?.putString(context.getString(R.string.freg_cache_exams), value)
        sEditor?.apply()
    }

    @JvmStatic
    fun setDay(context: Context, value: Int) {
        sEditor = sSharedPreferences?.edit()
        sEditor?.putInt(context.getString(R.string.day_pref), value)
        sEditor?.apply()
    }

    @JvmStatic
    fun getDay(context: Context): Int {
        return sSharedPreferences!!.getInt(context.getString(R.string.day_pref), 0)
    }

    @JvmStatic
    fun setWeek(context: Context, value: Int) {
        sEditor = sSharedPreferences?.edit()
        sEditor?.putInt(context.getString(R.string.week_pref), value)
        sEditor?.apply()
    }

    @JvmStatic
    fun getWeek(context: Context): Int {
        return sSharedPreferences!!.getInt(context.getString(R.string.week_pref), 0)
    }

    @JvmStatic
    fun getTheme(context: Context) : Int {
        return sSharedPreferences!!.getInt(context.getString(R.string.pref_theme), LIGHT)
    }

    @JvmStatic
    fun setTheme(context: Context, value: Int) {
        sEditor = sSharedPreferences?.edit()
        sEditor?.putInt(context.getString(R.string.pref_theme), value)
        sEditor?.apply()
    }
}