package com.titan.jnly.patrolv1.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.apt.ConserveTaskApt;
import com.titan.jnly.patrolv1.bean.ConserveTask;

public class ConserveTaskListAty extends BaseMvpCompatAty implements BaseRecycleAdapter.IViewClickListener<ConserveTask>, OnRefreshLoadMoreListener {


    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;

    private ConserveTaskApt conserveTaskApt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "施工任务", Gravity.CENTER);
        setContentView(R.layout.activity_conserve_task);
    }

    @Override
    protected void initialize() {
        rvList = $(R.id.conserve_task_recyclerview);
        swipeLayout = $(R.id.refreshLayout);
    }

    @Override
    protected void registerEvent() {
        swipeLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initClass() {
        conserveTaskApt = new ConserveTaskApt(rvList);
        conserveTaskApt.setIViewClickListener(this);
    }

    @Override
    public void onClick(View view, ConserveTask data, int position) {
        startActivity(new Intent(ConserveTaskListAty.this, ConserveTaskAty.class));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}