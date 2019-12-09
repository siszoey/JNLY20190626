package com.titan.jnly.patrolv1.ui.aty;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.titan.jnly.patrolv1.apt.PatrolTaskApt;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import java.util.List;

/*宣传任务列表页*/
public class PatrolTaskListAty extends BaseMvpCompatAty
        implements BaseRecycleAdapter.IViewClickListener<PatrolTask>,
        OnRefreshLoadMoreListener {

    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PatrolTaskApt patrolTaskApt;

    private PageParam page = PageParam.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_task);
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
        startActivity(new Intent(PatrolTaskListAty.this, PatrolTaskAty.class));
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
        netEasyReq.request(IPatrolApi.class, new NetWorkListen<TTResult<List<PatrolTask>>>() {
            @Override
            public void onSuccess(TTResult<List<PatrolTask>> data) {
                boolean flag = data.getResult();
                List list = data.getContent();
                finishReq(flag, list);
            }
        }).httpGetPatrolList(page.getNum(), page.getSize());
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
