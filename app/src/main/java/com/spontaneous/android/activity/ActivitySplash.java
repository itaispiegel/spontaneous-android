package com.spontaneous.android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

import com.facebook.FacebookSdk;
import com.spontaneous.android.R;
import com.spontaneous.android.util.AccountUtils;

public class ActivitySplash extends BaseActivity {

    private static final long SPLASH_DURATION = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        if(AccountUtils.isAuthenticated()) {
            startMainFlow();
        } else {
            startAuthenticationFlow();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    private void startAuthenticationFlow() {
        final BaseActivity baseActivity = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccountUtils.startAuthenticationFlow(baseActivity);
                finish();
            }
        }, SPLASH_DURATION);
    }

    private void startMainFlow() {
        final BaseActivity baseActivity = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccountUtils.startMainFlow(baseActivity);
                finish();
            }
        }, SPLASH_DURATION);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
