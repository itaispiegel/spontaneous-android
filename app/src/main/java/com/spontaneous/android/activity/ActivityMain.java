package com.spontaneous.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.ViewPagerAdapter;
import com.spontaneous.android.fragment.FragmentEvents;
import com.spontaneous.android.fragment.FragmentUserProfile;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;

/**
 * Main activity of the app.
 * Contains EventsFragment and UserProfileFragment.
 */
public class ActivityMain extends BaseActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;

    private ImageView mLoadingImage;
    private FloatingActionButton mCreateEventButton;

    private static FragmentUserProfile sUserProfileFragment;
    private static FragmentEvents sEventsFragment;

    static {
        sUserProfileFragment = new FragmentUserProfile();
        sEventsFragment = new FragmentEvents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), sEventsFragment, sUserProfileFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

        mCreateEventButton = (FloatingActionButton) findViewById(R.id.create_event_button);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class activityCreateEvent = ActivityCreateEvent.class;
                Intent intent = new Intent(getApplicationContext(), activityCreateEvent);

                Logger.info("Navigating to " + activityCreateEvent.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle options menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Do nothing for now
                return true;
            case R.id.logout:
                AccountUtils.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
