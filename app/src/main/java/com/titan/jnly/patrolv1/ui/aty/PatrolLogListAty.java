package com.titan.jnly.patrolv1.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.apt.PatrolLogApt;
import com.titan.jnly.patrolv1.bean.PatrolLog;

public class PatrolLogListAty extends BaseMvpCompatAty implements BaseRecycleAdapter.IViewClickListener<PatrolLog>, OnRefreshLoadMoreListener {


    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PatrolLogApt patrolLogApt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_aty_patrol_log);
    }

    @Override
    protected void initialize() {
        rvList = $(R.id.rvList);
        swipeLayout = $(R.id.swipeLayout);
    }

    @Override
    protected void registerEvent() {
        swipeLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initClass() {
        patrolLogApt = new PatrolLogApt(rvList);
        patrolLogApt.setIViewClickListener(this);
    }


    @Override
    public void onClick(View view, PatrolLog data, int position) {
        startActivity(new Intent(PatrolLogListAty.this, PatrolLogAty.class));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
