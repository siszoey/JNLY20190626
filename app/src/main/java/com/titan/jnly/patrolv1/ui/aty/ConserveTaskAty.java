package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.BaseFragmentAdapter;
import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.message.FuncManager;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.PatrolTask;
import com.titan.jnly.patrolv1.ui.frg.PatrolTaskDetailFrg;
import com.titan.jnly.patrolv1.ui.frg.PatrolTaskLogFrg;
import com.titan.jnly.patrolv1.ui.frg.PatrolTaskMsgFrg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class ConserveTaskAty extends BaseMvpCompatAty {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<BaseFragment> fragments;
    private PatrolTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) task = (PatrolTask) getIntent().getSerializableExtra("task");
        initTitle(R.drawable.ic_back, "巡查任务", Gravity.CENTER);
        setContentView(R.layout.patrolv1_ui_aty_conserve_task_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_right_add).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_right_add) {
            int index = tabLayout.getSelectedTabPosition();
            if (index == 1) {
                FuncManager.getInstance().invokeFunc(PatrolTaskLogFrg.FUN_ADD);
            }
            if (index == 2) {
                FuncManager.getInstance().invokeFunc(PatrolTaskMsgFrg.FUN_ADD);
            }
        }
        return super.onOptionsItemSelected(item);
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
        fragments.add(PatrolTaskDetailFrg.newInstance(task));
        fragments.add(PatrolTaskLogFrg.newInstance());
        fragments.add(PatrolTaskMsgFrg.newInstance());
        BaseFragmentAdapter.create(viewPager, fragments);
        for (BaseFragment fragment : fragments) {
            tabLayout.addTab(tabLayout.newTab().setText(fragment.getName()));
        }
        tabLayout.setupWithViewPager(viewPager);
    }
}