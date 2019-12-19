package com.titan.jnly.patrolv1.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.util.PageParam;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.api.IPatrolApi;
import com.titan.jnly.patrolv1.apt.ConserveTaskApt;
import com.titan.jnly.patrolv1.bean.ConserveTask;

import java.util.List;

public class ConserveTaskListAty extends BaseMvpCompatAty
        implements BaseRecycleAdapter.IViewClickListener<ConserveTask>, OnRefreshLoadMoreListener {


    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private ConserveTaskApt conserveTaskApt;
    private PageParam page = PageParam.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "施工任务", Gravity.CENTER);
        setContentView(R.layout.patrolv1_ui_aty_conserve_task);
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
        conserveTaskApt = new ConserveTaskApt(rvList);
        conserveTaskApt.setIViewClickListener(this);
        requestList();
    }

    @Override
    public void onClick(View view, ConserveTask data, int position) {
        Intent intent = new Intent(_context, ConserveTaskAty.class);
        intent.putExtra("task", data);
        startActivity(intent);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page.next();
        requestList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page.New();
        requestList();
    }

    public void requestList() {
        netEasyReq.request(IPatrolApi.class, new NetWorkListen<TTResult<List<ConserveTask>>>() {
            @Override
            public void onSuccess(TTResult<List<ConserveTask>> data) {
                boolean flag = data.getResult();
                List list = data.getContent();
                finishReq(flag, list);
            }
        }).httpGetConserveTaskList(page.getNum(), page.getSize());
    }

    private void finishReq(boolean flag, List data) {
        if (page.isNew()) {
            swipeLayout.finishRefresh(flag);
            if (flag) conserveTaskApt.replaceAll(data);
        } else {
            swipeLayout.finishLoadMore(flag);
            if (flag) conserveTaskApt.appendList(data);
        }
    }
}
