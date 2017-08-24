package com.mai.nix.maiapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private String mCurrentGroup;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSharedPreferences = getSharedPreferences("suka", Context.MODE_PRIVATE);
        mCurrentGroup = mSharedPreferences.getString(getString(R.string.pref_group), "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentGroup.isEmpty()) {
                    mIntent = ChooseGroupActivity.newIntent(SplashActivity.this, true);
                }else{
                    mIntent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(mIntent);
                finish();
            }
        }, 550);
    }
}