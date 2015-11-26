package com.spontaneous.android.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.ViewPagerAdapter;
import com.spontaneous.android.fragment.FragmentEvents;
import com.spontaneous.android.fragment.FragmentUserProfile;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.ActivityUtils;
import com.spontaneous.android.util.Logger;

/**
 * Main activity of the app.
 * Contains EventsFragment and UserProfileFragment.
 */
public class ActivityMain extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;
    private ActionBar actionBar;

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
        ActivityUtils.setCustomActionBar(this);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), sEventsFragment, sUserProfileFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

        //Set action bar tabs
        actionBar = getActionBar();

        if (actionBar != null) {
            ColorDrawable lightBlue = new ColorDrawable(getResources().getColor(R.color.light_blue));

            actionBar.setStackedBackgroundDrawable(lightBlue);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab().setIcon(R.drawable.tab_home_selector).setTabListener(this));
            actionBar.addTab(actionBar.newTab().setIcon(R.drawable.tab_profile_selector).setTabListener(this));

            // PageChange Listener
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                    ActivityUtils.hideKeyboard(ActivityMain.this);
                }
            });
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        actionBar.setSelectedNavigationItem(tab.getPosition());
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}
