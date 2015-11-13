package com.spontaneous.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.spontaneous.android.R;
import com.spontaneous.android.util.UIUtils;

/**
 * Main activity of the app.
 * Contains EventsFragment and UserProfileFragment.
 */
public class ActivityMain extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.setCustomActionBar(this);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.animate_fade_out);
    }
}
