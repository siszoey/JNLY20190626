package com.titan.jnly.task.ui.frg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.utils.NotifyArrayList;
import com.lib.bandaid.utils.ViewUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.titan.jnly.R;
import com.titan.jnly.common.fragment.BaseMainFragment;
import com.titan.jnly.task.apt.DataSyncAdapter;
import com.titan.jnly.vector.enums.DataStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SyncUnFragment extends BaseMainFragment
        implements OnRefreshListener,
        OnLoadMoreListener,
        View.OnClickListener,
        BaseRecycleAdapter.IViewClickListener<Feature>,
        BaseRecycleAdapter.ILongViewClickListener<Feature>,
        NotifyArrayList.IListener {

    public static SyncUnFragment newInstance() {
        Bundle args = new Bundle();
        SyncUnFragment fragment = new SyncUnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int pageSize;
    private int pageNum;
    private Context context;
    private Activity activity;
    private View view;
    private TextView tvCount, tvClear;
    private SmartRefreshLayout swipeLayout;
    private RecyclerView rvList;
    private FeatureTable table;
    private DataSyncAdapter adapter;

    public SyncUnFragment setTable(FeatureTable table) {
        this.table = table;
        return this;
    }

    public SyncUnFragment setPageSize(int pageSize, int pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            context = getContext();
            activity = getActivity();
            view = inflater.inflate(R.layout.task_ui_frg_sync_all, container, false);
            initialize();
            registerEvent();
            initClass();
        }
        return view;
    }

    protected void initialize() {
        tvCount = ViewUtil.findViewById(view, R.id.tvCount);
        tvClear = ViewUtil.findViewById(view, R.id.tvClear);
        swipeLayout = ViewUtil.findViewById(view, R.id.swipeLayout);
        rvList = ViewUtil.findViewById(view, R.id.rvList);
    }

    protected void registerEvent() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadMoreListener(this);
        tvClear.setOnClickListener(this);
    }

    protected void initClass() {
        adapter = new DataSyncAdapter(rvList, this);
        adapter.setIViewClickListener(this);
        adapter.setILongViewClickListener(this);
        queryData(pageNum);
    }

    @Override
    public void onClick(View view, Feature data, int position) {

    }

    @Override
    public void onLongViewClick(View view, Feature data, int position) {

    }

    @Override
    public void itemChange() {
        tvCount.setText(adapter.getSelCount() + "");
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNum = 1;
        adapter.removeAll();
        queryData(pageNum);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        queryData(++pageNum);
    }

    private void finishHandle(boolean success) {
        if (pageNum == 1) {
            swipeLayout.finishRefresh(success);
        } else {
            swipeLayout.finishLoadMore(success);
        }
    }


    @Override
    public void onClick(View v) {

    }

    private void queryData(int pageNum) {
        QueryParameters params = new QueryParameters();
        params.setReturnGeometry(true);
        params.setWhereClause(DataStatus.SYNC_UN());
        params.getOrderByFields().add(new QueryParameters.OrderBy("OBJECTID", QueryParameters.SortOrder.DESCENDING));
        params.setMaxFeatures(pageSize);
        params.setResultOffset((pageNum - 1) * pageSize);
        ListenableFuture<FeatureQueryResult> listen = table.queryFeaturesAsync(params);
        listen.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Feature> features = new ArrayList<>();
                    FeatureQueryResult result = listen.get();
                    Iterator<Feature> iterable = result.iterator();
                    while (iterable.hasNext()) {
                        features.add(iterable.next());
                    }
                    adapter.appendList(features);
                    finishHandle(true);
                } catch (Exception e) {
                    finishHandle(false);
                }
            }
        });
    }
}
