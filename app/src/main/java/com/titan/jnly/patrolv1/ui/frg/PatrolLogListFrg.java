package com.titan.jnly.patrolv1.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.message.FuncManager;
import com.lib.bandaid.message.FuncNoParamNoResult;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.PageParam;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.api.IPatrolApiLocal;
import com.titan.jnly.patrolv1.apt.PatrolLogApt;
import com.titan.jnly.patrolv1.bean.PatrolLog;
import com.titan.jnly.patrolv1.ui.aty.PatrolLogAty;

import java.util.List;

public class PatrolLogListFrg extends BaseFragment
        implements OnRefreshLoadMoreListener,
        BaseRecycleAdapter.IViewClickListener<PatrolLog>,
        BaseRecycleAdapter.ILongViewClickListener<PatrolLog> {

    public final static String FUN_ADD = "FUN_ADD_PATROL_TASK_LOG";

    public static PatrolLogListFrg newInstance() {
        PatrolLogListFrg fragment = new PatrolLogListFrg();
        fragment.name = "巡查日志";
        return fragment;
    }

    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PatrolLogApt patrolLogApt;
    private PageParam page = PageParam.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        patrolLogApt.setILongViewClickListener(this);
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
        netEasyReq.request(IPatrolApiLocal.class, new NetWorkListen<TTResult<List<PatrolLog>>>() {
            @Override
            public void onSuccess(TTResult<List<PatrolLog>> data) {
                boolean flag = data.getResult();
                List list = data.getContent();
                finishReq(flag, list);
            }
        }).httpGetPatrolLogList(page.getNum(), page.getSize());
    }

    private void finishReq(boolean flag, List data) {
        if (page.isNew()) {
            swipeLayout.finishRefresh(flag);
            if (flag) patrolLogApt.replaceAll(data);
        } else {
            swipeLayout.finishLoadMore(flag);
            if (flag) patrolLogApt.appendList(data);
        }
    }

    void dispatchFun() {
        FuncManager.getInstance().addFunc(new FuncNoParamNoResult(FUN_ADD) {
            @Override
            public void function() {
                startActivity(new Intent(getContext(), PatrolLogAty.class));
            }
        });
    }

    @Override
    public void onClick(View view, PatrolLog data, int position) {
        startActivity(new Intent(getContext(), PatrolLogAty.class));
    }

    @Override
    public void onLongViewClick(View view, PatrolLog data, int position) {
        new ATEDialog.Theme_Alert(context)
                .title("提示")
                .content("确认删除？")
                .positiveText("删除")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        reqDel(data);
                    }
                }).show();
    }

    public void reqDel(PatrolLog log) {
        netEasyReq.request(IPatrolApiLocal.class, new NetWorkListen<TTResult<Boolean>>() {
            @Override
            public void onSuccess(TTResult<Boolean> data) {
                boolean flag = data.getResult();
                if (flag) patrolLogApt.removeItem(log);
                else showLongToast("删除失败！");
            }
        }).httpPostPatrolLogDel(log);
    }
}
