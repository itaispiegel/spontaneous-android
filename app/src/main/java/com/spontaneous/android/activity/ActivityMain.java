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
import com.spontaneous.android.model.Event;
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

    private FragmentUserProfile mUserProfileFragment;
    private FragmentEvents mEventsFragment;

    private void initFragments() {
        mUserProfileFragment = new FragmentUserProfile();
        mEventsFragment = new FragmentEvents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFragments();

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mEventsFragment, mUserProfileFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

        mCreateEventButton = (FloatingActionButton) findViewById(R.id.create_event_button);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class activity = ActivityCreateEvent.class;
                Intent intent = new Intent(getApplicationContext(), ActivityCreateEvent.class);

                Logger.info("Navigating to " + activity.getName());
                startActivityForResult(intent, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String extraName = getString(R.string.created_event_intent_extra);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Event event = (Event) data.getSerializableExtra(extraName);
                mEventsFragment.addEvent(event);
            }
        }
    }
}
