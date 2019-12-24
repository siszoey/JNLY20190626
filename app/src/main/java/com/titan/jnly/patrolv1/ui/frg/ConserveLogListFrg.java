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
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.PageParam;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.api.IPatrolApiLocal;
import com.titan.jnly.patrolv1.apt.ConserveLogApt;
import com.titan.jnly.patrolv1.bean.ConserveLog;
import com.titan.jnly.patrolv1.ui.aty.ConserveLogAty;

import java.util.List;

public class ConserveLogListFrg extends BaseFragment
        implements OnRefreshLoadMoreListener,
        BaseRecycleAdapter.IViewClickListener<ConserveLog>,
        BaseRecycleAdapter.ILongViewClickListener<ConserveLog> {

    public static ConserveLogListFrg newInstance() {
        ConserveLogListFrg fragment = new ConserveLogListFrg();
        fragment.name = "施工日志列表";
        return fragment;
    }

    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private ConserveLogApt conserveLogApt;
    private PageParam page = PageParam.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_aty_conserve_log);
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
        conserveLogApt = new ConserveLogApt(rvList);
        conserveLogApt.setIViewClickListener(this);
        conserveLogApt.setILongViewClickListener(this);
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
        netEasyReq.request(IPatrolApiLocal.class, new NetWorkListen<TTResult<List<ConserveLog>>>() {
            @Override
            public void onSuccess(TTResult<List<ConserveLog>> data) {
                boolean flag = data.getResult();
                List list = data.getContent();
                finishReq(flag, list);
            }
        }).httpGetConserveLogList(page.getNum(), page.getSize());
    }

    private void finishReq(boolean flag, List data) {
        if (page.isNew()) {
            swipeLayout.finishRefresh(flag);
            if (flag) conserveLogApt.replaceAll(data);
        } else {
            swipeLayout.finishLoadMore(flag);
            if (flag) conserveLogApt.appendList(data);
        }
    }

    @Override
    public void onClick(View view, ConserveLog data, int position) {
        Intent intent = new Intent(getContext(), ConserveLogAty.class);
        intent.putExtra("log", data);
        startActivity(intent);
    }

    @Override
    public void onLongViewClick(View view, ConserveLog data, int position) {
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

    public void reqDel(ConserveLog msg) {
        netEasyReq.request(IPatrolApiLocal.class, new NetWorkListen<TTResult<Boolean>>() {
            @Override
            public void onSuccess(TTResult<Boolean> data) {
                boolean flag = data.getResult();
                if (flag) conserveLogApt.removeItem(msg);
                else showLongToast("删除失败！");
            }
        }).httpPostConserveLogDel(msg);
    }
}
