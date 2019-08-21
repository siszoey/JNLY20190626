package com.titan.jnly.task.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.google.android.material.tabs.TabLayout;
import com.titan.jnly.R;
import com.titan.jnly.common.activity.BaseFragmentAty;
import com.titan.jnly.common.activity.BaseMvpFrgAty;
import com.titan.jnly.common.fragment.BaseMainFragment;
import com.titan.jnly.task.apt.SyncFrgAdapter;
import com.titan.jnly.task.ui.frg.SyncAllFragment;
import com.titan.jnly.task.ui.frg.SyncEdFragment;
import com.titan.jnly.task.ui.frg.SyncUnFragment;
import com.titan.jnly.vector.enums.DataStatus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class DataSyncAtyV1 extends BaseMvpFrgAty
        implements
        BaseMainFragment.OnBackToFirstListener {

    private int pageSize = 30;
    private int pageNum = 1;
    private FeatureTable table;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SyncFrgAdapter frgAdapter;
    private Fragment[] fragments;

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
        fragments = new Fragment[3];
        fragments[0] = SyncEdFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        fragments[1] = SyncUnFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        fragments[2] = SyncAllFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        frgAdapter = new SyncFrgAdapter(getSupportFragmentManager(), fragments, new String[]{"已上传", "未上传", "全部数据"});
        viewPager.setAdapter(frgAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("已上传"));
        tabLayout.addTab(tabLayout.newTab().setText("未上传"));
        tabLayout.addTab(tabLayout.newTab().setText("全部数据"));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();
    }

    @Override
    public void onBackToFirstFragment() {
        finish();
    }

    public void dispatchData(Feature... features) {
        if (features == null || features.length == 0) return;
        SyncEdFragment syncEdFragment = (SyncEdFragment) fragments[0];
        SyncUnFragment syncUnFragment = (SyncUnFragment) fragments[1];
        SyncAllFragment syncAllFragment = (SyncAllFragment) fragments[2];
        Feature feature;
        for (int i = 0; i < features.length; i++) {
            feature = features[i];
            if (feature == null) continue;
            if (DataStatus.isEdit(feature)) {
                syncEdFragment.getAdapter().removeData(feature);
                syncUnFragment.getAdapter().insertItem(0, feature);
            }
            if (DataStatus.isSync(feature)) {
                syncEdFragment.getAdapter().insertItem(0, feature);
                syncUnFragment.getAdapter().removeData(feature);
            }
            syncAllFragment.getAdapter().updateData(feature);
        }
    }

    public void dispatchData(List<Feature> features) {
        if (features == null || features.size() == 0) return;
        SyncEdFragment syncEdFragment = (SyncEdFragment) fragments[0];
        SyncUnFragment syncUnFragment = (SyncUnFragment) fragments[1];
        SyncAllFragment syncAllFragment = (SyncAllFragment) fragments[2];
        Feature feature;
        for (int i = 0; i < features.size(); i++) {
            feature = features.get(i);
            if (feature == null) continue;
            if (DataStatus.isEdit(feature)) {
                syncEdFragment.getAdapter().removeData(feature);
                syncUnFragment.getAdapter().insertItem(0, feature);
            }
            if (DataStatus.isSync(feature)) {
                syncEdFragment.getAdapter().insertItem(0, feature);
                syncUnFragment.getAdapter().removeData(feature);
            }
            syncAllFragment.getAdapter().updateData(feature);
        }
    }


}
