package com.lib.bandaid.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.util.AppUtil;
import com.lib.bandaid.util.CollectUtil;

import java.util.List;

public class BaseFragmentAdapter extends FragmentStatePagerAdapter {

    public static BaseFragmentAdapter create(Context context, List<BaseFragment> fragments) {
        FragmentManager fm = AppUtil.getFragmentManagerX(context);
        return new BaseFragmentAdapter(fm, fragments);
    }

    public static BaseFragmentAdapter create(Context context, BaseFragment... fragments) {
        FragmentManager fm = AppUtil.getFragmentManagerX(context);
        return new BaseFragmentAdapter(fm, fragments);
    }

    public static BaseFragmentAdapter create(ViewPager viewPager, List<BaseFragment> fragments) {
        Context context = viewPager.getContext();
        FragmentManager fm = AppUtil.getFragmentManagerX(context);
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(fm, fragments);
        viewPager.setAdapter(adapter);
        return adapter;
    }

    public static BaseFragmentAdapter create(ViewPager viewPager, BaseFragment... fragments) {
        Context context = viewPager.getContext();
        FragmentManager fm = AppUtil.getFragmentManagerX(context);
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(fm, fragments);
        viewPager.setAdapter(adapter);
        return adapter;
    }

    public List<BaseFragment> fragments;

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public BaseFragmentAdapter(FragmentManager fm, BaseFragment... fragments) {
        super(fm);
        this.fragments = CollectUtil.array2List(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getName();
    }
}

