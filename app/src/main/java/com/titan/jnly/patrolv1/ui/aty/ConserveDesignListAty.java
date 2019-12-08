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
import com.titan.jnly.patrolv1.apt.ConserveDesignApt;
import com.titan.jnly.patrolv1.bean.ConserveTask;


public class ConserveDesignListAty extends BaseMvpCompatAty implements BaseRecycleAdapter.IViewClickListener<ConserveTask>, OnRefreshLoadMoreListener {

    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private ConserveDesignApt conserveLogApt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "施工设计", Gravity.CENTER);
        setContentView(R.layout.activity_conserve_log);

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
        conserveLogApt = new ConserveDesignApt(rvList);
        conserveLogApt.setIViewClickListener(this);
    }

    @Override
    public void onClick(View view, ConserveTask data, int position) {
        startActivity(new Intent(ConserveDesignListAty.this, ConserveDesignAty.class));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
