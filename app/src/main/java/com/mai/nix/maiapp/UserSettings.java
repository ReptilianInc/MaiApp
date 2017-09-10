package com.mai.nix.maiapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nix on 08.09.2017.
 */

public class UserSettings {
    private static SharedPreferences sSharedPreferences;
    private static SharedPreferences.Editor sEditor;

    public static final String WITH_APP = "1";
    public static final String ONLY_BROWSER = "2";
    public static final String EVERY_DAY = "1";
    public static final String EVERY_WEEK = "2";
    public static final String ONLY_FORCIBLY = "3";

    public static void initialize(Context context){
        sSharedPreferences = context.getSharedPreferences("suka", Context.MODE_PRIVATE);
    }
    public static String getGroup(Context context){
        return sSharedPreferences.getString(context.getString(R.string.pref_group),
                context.getString(R.string.pref_group_summary));
    }
    public static void setGroup(Context context, String group){
        sEditor = sSharedPreferences.edit();
        sEditor.putString(context.getString(R.string.pref_group), group);
        sEditor.apply();
    }
    public static String getLinksPreference(Context context){
        return sSharedPreferences.getString(context.getString(R.string.pref_links),
                WITH_APP);
    }
    public static void setLinksPreference(Context context, String value){
        sEditor = sSharedPreferences.edit();
        sEditor.putString(context.getString(R.string.pref_links), value);
        sEditor.apply();
    }
    public static String getSubjectsUpdateFrequency(Context context){
        return sSharedPreferences.getString(context.getString(R.string.freg_cache),
                ONLY_FORCIBLY);
    }
    public static void setSubjectsUpdateFrequency(Context context, String value){
        sEditor = sSharedPreferences.edit();
        sEditor.putString(context.getString(R.string.freg_cache), value);
        sEditor.apply();
    }
    public static String getExamsUpdateFrequency(Context context){
        return sSharedPreferences.getString(context.getString(R.string.freg_cache_exams),
                ONLY_FORCIBLY);
    }
    public static void setExamsUpdateFrequency(Context context, String value){
        sEditor = sSharedPreferences.edit();
        sEditor.putString(context.getString(R.string.freg_cache_exams), value);
        sEditor.apply();
    }

    public static void setDay(Context context, int value){
        sEditor = sSharedPreferences.edit();
        sEditor.putInt(context.getString(R.string.day_pref), value);
        sEditor.apply();
    }
    public static int getDay(Context context){
        return sSharedPreferences.getInt(context.getString(R.string.day_pref), 0);
    }

    public static void setWeek(Context context, int value){
        sEditor = sSharedPreferences.edit();
        sEditor.putInt(context.getString(R.string.week_pref), value);
        sEditor.apply();
    }
    public static int getWeek(Context context){
        return sSharedPreferences.getInt(context.getString(R.string.week_pref), 0);
    }
}
