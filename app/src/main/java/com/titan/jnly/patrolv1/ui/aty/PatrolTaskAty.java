package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.google.android.material.tabs.TabLayout;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.BaseFragmentAdapter;
import com.titan.jnly.R;
import com.lib.bandaid.fragment.BaseFragment;
import com.titan.jnly.patrolv1.ui.frg.PatrolTaskDetailFrg;
import com.titan.jnly.patrolv1.ui.frg.PatrolTaskLogFrg;
import com.titan.jnly.patrolv1.ui.frg.PatrolTaskMsgFrg;

import java.util.ArrayList;
import java.util.List;

/*巡查任务详情页*/
public class PatrolTaskAty extends BaseMvpCompatAty {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "巡查任务", Gravity.CENTER);
        setContentView(R.layout.patrolv1_ui_aty_patrol_task_details);
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
        fragments = new ArrayList<>();
        fragments.add(PatrolTaskDetailFrg.newInstance());
        fragments.add(PatrolTaskLogFrg.newInstance());
        fragments.add(PatrolTaskMsgFrg.newInstance());
        BaseFragmentAdapter.create(viewPager, fragments);
        for (BaseFragment fragment : fragments) {
            tabLayout.addTab(tabLayout.newTab().setText(fragment.getName()));
        }
        tabLayout.setupWithViewPager(viewPager);
    }
}
