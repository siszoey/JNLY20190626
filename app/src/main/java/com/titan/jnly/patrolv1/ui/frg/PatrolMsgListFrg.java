package com.titan.jnly.patrolv1.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.message.FuncManager;
import com.lib.bandaid.message.FuncNoParamNoResult;
import com.lib.bandaid.util.PageParam;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.api.IPatrolApi;
import com.titan.jnly.patrolv1.apt.PatrolMsgApt;
import com.titan.jnly.patrolv1.bean.PatrolMsg;
import com.titan.jnly.patrolv1.ui.aty.PatrolMsgAty;

import java.util.List;

public class PatrolMsgListFrg extends BaseFragment
        implements OnRefreshLoadMoreListener, BaseRecycleAdapter.IViewClickListener<PatrolMsg> {

    public final static String FUN_ADD = "FUN_ADD_PATROL_TASK_MSG";

    public static PatrolMsgListFrg newInstance() {
        PatrolMsgListFrg fragment = new PatrolMsgListFrg();
        fragment.name = "巡查消息";
        return fragment;
    }

    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PatrolMsgApt patrolMsgApt;
    private PageParam page = PageParam.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_aty_patrol_msg);
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
        patrolMsgApt = new PatrolMsgApt(rvList);
        patrolMsgApt.setIViewClickListener(this);
        dispatchFun();
        requestList();
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
        netEasyReq.request(IPatrolApi.class, new NetWorkListen<TTResult<List<PatrolMsg>>>() {
            @Override
            public void onSuccess(TTResult<List<PatrolMsg>> data) {
                boolean flag = data.getResult();
                List list = data.getContent();
                finishReq(flag, list);
            }
        }).httpGetPatrolMsgList(page.getNum(), page.getSize());
    }

    private void finishReq(boolean flag, List data) {
        if (page.isNew()) {
            swipeLayout.finishRefresh(flag);
            if (flag) patrolMsgApt.replaceAll(data);
        } else {
            swipeLayout.finishLoadMore(flag);
            if (flag) patrolMsgApt.appendList(data);
        }
    }

    void dispatchFun() {
        FuncManager.getInstance().addFunc(new FuncNoParamNoResult(FUN_ADD) {
            @Override
            public void function() {
                startActivity(new Intent(getContext(), PatrolMsgAty.class));
            }
        });
    }

    @Override
    public void onClick(View view, PatrolMsg data, int position) {
        startActivity(new Intent(getContext(), PatrolMsgAty.class));
    }
}
