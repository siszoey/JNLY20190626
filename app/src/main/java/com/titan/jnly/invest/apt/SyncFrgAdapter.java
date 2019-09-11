package com.titan.jnly.invest.apt;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SyncFrgAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private Fragment[] fragments;

    public SyncFrgAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
