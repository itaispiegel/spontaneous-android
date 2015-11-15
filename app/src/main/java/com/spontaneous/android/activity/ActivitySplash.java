package com.spontaneous.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.facebook.FacebookSdk;
import com.spontaneous.android.R;
import com.spontaneous.android.util.AccountUtils;

public class ActivitySplash extends Activity {

    private static final long SPLASH_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animate_fade_in, R.anim.animate_fade_out);
        setContentView(R.layout.activity_splash);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if(AccountUtils.isAuthenticated()) {
            startMainFlow();
        } else {
            startAuthenticationFlow();
        }
    }

    private void startAuthenticationFlow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccountUtils.startAuthenticationFlow(getApplicationContext());
                finish();
            }
        }, SPLASH_DURATION);
    }

    private void startMainFlow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccountUtils.startMainFlow(getApplicationContext());
                finish();
            }
        }, SPLASH_DURATION);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
