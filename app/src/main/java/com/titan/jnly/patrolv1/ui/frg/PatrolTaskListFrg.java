package com.titan.jnly.patrolv1.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.util.PageParam;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.common.bean.PageRes;
import com.titan.jnly.patrolv1.api.IPatrolApi;
import com.titan.jnly.patrolv1.api.IPatrolApiLocal;
import com.titan.jnly.patrolv1.apt.PatrolTaskApt;
import com.titan.jnly.patrolv1.bean.PatrolTask;
import com.titan.jnly.patrolv1.ui.aty.PatrolTaskAty;
import com.titan.jnly.system.Constant;

import java.util.List;

/**
 * 巡查任务列表
 */
public class PatrolTaskListFrg extends BaseFragment
        implements BaseRecycleAdapter.IViewClickListener<PatrolTask>,
        OnRefreshLoadMoreListener {

    public static PatrolTaskListFrg newInstance() {
        PatrolTaskListFrg fragment = new PatrolTaskListFrg();
        fragment.name = "巡查任务列表";
        return fragment;
    }

    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PatrolTaskApt patrolTaskApt;
    private PageParam page = PageParam.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_aty_patrol_task);
    }

    @Override
    protected void initialize() {
        swipeLayout = $(R.id.swipeLayout);
        rvList = $(R.id.rvList);
    }

    @Override
    protected void registerEvent() {
        swipeLayout.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initClass() {
        patrolTaskApt = new PatrolTaskApt(rvList);
        patrolTaskApt.setIViewClickListener(this);
        requestList();
    }

    @Override
    public void onClick(View view, PatrolTask data, int position) {
        Intent intent = new Intent(context, PatrolTaskAty.class);
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
        netEasyReq.request(IPatrolApi.class, (NetWorkListen<TTResult<PageRes<PatrolTask>>>) data -> {
            boolean flag = data.getResult();
            List list = data.getContent().getRows();
            finishReq(flag, list);
        }).httpGetPatrolTaskList("", page.getNum(), page.getSize(), Constant.getUserInfo().getId(), "");
    }

    private void finishReq(boolean flag, List data) {
        if (page.isNew()) {
            swipeLayout.finishRefresh(flag);
            if (flag) patrolTaskApt.replaceAll(data);
        } else {
            swipeLayout.finishLoadMore(flag);
            if (flag) patrolTaskApt.appendList(data);
        }
    }
}
