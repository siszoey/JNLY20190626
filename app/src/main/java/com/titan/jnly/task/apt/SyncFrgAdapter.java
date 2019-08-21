package com.titan.jnly.task.apt;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.esri.arcgisruntime.data.FeatureTable;
import com.titan.jnly.task.ui.frg.SyncAllFragment;
import com.titan.jnly.task.ui.frg.SyncEdFragment;
import com.titan.jnly.task.ui.frg.SyncUnFragment;

public class SyncFrgAdapter extends FragmentPagerAdapter {

    private int pageSize = 30;
    private int pageNum = 1;

    private FeatureTable table;
    private String[] titles;

    public SyncFrgAdapter(FragmentManager fm, FeatureTable table, String... titles) {
        super(fm);
        this.table = table;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return SyncEdFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        } else if (position == 1) {
            return SyncUnFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        } else {
            return SyncAllFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        }
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
