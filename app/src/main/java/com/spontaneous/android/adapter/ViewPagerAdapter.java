package com.spontaneous.android.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * ViewPager adapter
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * List of fragments
     */
    private Fragment[] mFragment;

    public ViewPagerAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.mFragment = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment[position];
    }

    @Override
    public int getCount() {
        return mFragment.length;
    }
}

