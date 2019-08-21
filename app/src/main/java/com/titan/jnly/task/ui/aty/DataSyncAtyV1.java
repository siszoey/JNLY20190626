package com.titan.jnly.task.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;

import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.data.FeatureTable;
import com.google.android.material.tabs.TabLayout;
import com.titan.jnly.R;
import com.titan.jnly.common.activity.BaseFragmentAty;
import com.titan.jnly.common.fragment.BaseMainFragment;
import com.titan.jnly.task.apt.SyncFrgAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DataSyncAtyV1 extends BaseFragmentAty
        implements
        BaseMainFragment.OnBackToFirstListener {

    private FeatureTable table;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void getTable(FeatureTable table) {
        this.table = table;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "数据上传", Gravity.CENTER);
        setContentView(R.layout.task_ui_aty_data_sync_v1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_right_up_load).setVisible(true);
        return true;
    }

    @Override
    protected void initialize() {
        tabLayout = $(R.id.tabLayout);
        viewPager = $(R.id.viewPager);

    }

    @Override
    protected void registerEvent() {
    }

    @Override
    protected void initClass() {
        viewPager.setAdapter(new SyncFrgAdapter(getSupportFragmentManager(), table, "已上传", "未上传", "全部数据"));
        tabLayout.addTab(tabLayout.newTab().setText("已上传"));
        tabLayout.addTab(tabLayout.newTab().setText("未上传"));
        tabLayout.addTab(tabLayout.newTab().setText("全部数据"));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();


    }

    @Override
    public void onBackToFirstFragment() {

    }
}
