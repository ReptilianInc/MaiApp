package com.mai.nix.maiapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private String mCurrentGroup;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UserSettings.initialize(SplashActivity.this);
        mCurrentGroup = UserSettings.getGroup(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentGroup == null) {
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