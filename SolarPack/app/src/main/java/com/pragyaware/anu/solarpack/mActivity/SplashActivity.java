package com.pragyaware.anu.solarpack.mActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.pragyaware.anu.solarpack.R;
import com.pragyaware.anu.solarpack.mUtil.PreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (PreferencesUtil.getInstance(SplashActivity.this).isLoggedIn()) {

                    if (PreferencesUtil.getInstance(SplashActivity.this).isOfficer()) {
                        startActivity(new Intent(SplashActivity.this, SiteEngineer.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }

                } else {
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                }
            }
        }, 2000);

    }
}
