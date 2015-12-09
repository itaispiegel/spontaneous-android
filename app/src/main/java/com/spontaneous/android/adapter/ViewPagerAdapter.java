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
    private final Fragment[] mFragments;

    public ViewPagerAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}

