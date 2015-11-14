package com.spontaneous.android.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.ViewPagerAdapter;
import com.spontaneous.android.fragment.FragmentUserProfile;
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
}
