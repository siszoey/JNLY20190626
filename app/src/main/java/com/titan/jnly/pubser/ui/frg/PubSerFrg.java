package com.titan.jnly.pubser.ui.frg;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.util.ClickUtil;
import com.lib.bandaid.util.PageParam;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.titan.jnly.R;
import com.titan.jnly.common.activity.ComRhtAty;
import com.titan.jnly.pubser.api.IPublicApi;
import com.titan.jnly.pubser.apt.PublishApt;
import com.titan.jnly.pubser.bean.ItemContent;
import com.titan.jnly.pubser.bean.Publish;
import com.titan.jnly.pubser.bean.PublishPage;
import com.zy.foxui.util.ObjectUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共服务Frg 模板
 */
public class PubSerFrg extends BaseFragment
        implements OnRefreshLoadMoreListener,
        BaseRecycleAdapter.IViewClickListener<Publish> {

    public static PubSerFrg newInstance(String title, int type) {
        PubSerFrg fragment = new PubSerFrg();
        fragment.name = title;

        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);

        return fragment;
    }

    private int type;
    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PublishApt publishApt;
    private PageParam page = PageParam.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pubser_ui_frg_main_layout);
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
        type = getArguments().getInt("type");
        publishApt = new PublishApt(rvList);
        publishApt.setIViewClickListener(this);
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
        netEasyReq.request(IPublicApi.class, new NetWorkListen<TTResult<PublishPage>>() {
            @Override
            public void onSuccess(TTResult<PublishPage> data) {
                boolean flag = data.getResult();
                List<Publish> list = data.getContent().getRows();
                finishReq(flag, list);
            }
        }).httpGetList(type, "", page.getSize(), page.getNum());
    }

    private void finishReq(boolean flag, List data) {
        if (page.isNew()) {
            swipeLayout.finishRefresh(flag);
            if (flag) publishApt.replaceAll(data);
        } else {
            swipeLayout.finishLoadMore(flag);
            if (flag) publishApt.appendList(data);
        }
    }


    @Override
    public void onClick(View view, Publish data, int position) {
        if (ClickUtil.isFastDoubleClick(view)) return;
        reqDetail(data);
    }

    void reqDetail(Publish publish) {
        netEasyReq.request(IPublicApi.class, (NetWorkListen<TTResult<ItemContent>>) data -> {
            ItemContent content = data.getContent();
            ComRhtAty.start(activity, content);
        }).httpGetDetail(publish.getId(), type);
    }

}
