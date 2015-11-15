package com.spontaneous.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.ViewPagerAdapter;
import com.spontaneous.android.fragment.FragmentUserProfile;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.UIUtils;

/**
 * Main activity of the app.
 * Contains EventsFragment and UserProfileFragment.
 */
public class ActivityMain extends FragmentActivity {

    private ViewPager mViewPager;
    private ViewPagerAdapter mPagerAdapter;

    private ImageView mLoadingImage;
    private FloatingActionButton mCreateEventButton;

    private FragmentUserProfile fragmentUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.setCustomActionBar(this);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.animate_fade_out);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new FragmentUserProfile());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);
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
}
