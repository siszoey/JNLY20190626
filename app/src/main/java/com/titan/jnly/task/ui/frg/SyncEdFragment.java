package com.titan.jnly.task.ui.frg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.arcruntime.util.FeatureUtil;
import com.lib.bandaid.arcruntime.util.TransformUtil;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.NotifyArrayList;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.ViewUtil;
import com.lib.bandaid.widget.collect.image.CollectImgBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.titan.jnly.R;
import com.titan.jnly.common.fragment.BaseMvpFragment;
import com.titan.jnly.system.Constant;
import com.titan.jnly.task.apt.DataSyncAdapter;
import com.titan.jnly.task.bean.DataSync;
import com.titan.jnly.task.ui.aty.DataSyncAtyV1;
import com.titan.jnly.task.ui.aty.SyncContract;
import com.titan.jnly.task.ui.aty.SyncPresenter;
import com.titan.jnly.vector.enums.DataStatus;
import com.titan.jnly.vector.ui.aty.SingleEditActivity;
import com.titan.jnly.vector.util.MultiCompute;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SyncEdFragment
        extends BaseMvpFragment
        implements OnRefreshListener,
        OnLoadMoreListener,
        View.OnClickListener,
        BaseRecycleAdapter.IViewClickListener<Feature>,
        BaseRecycleAdapter.ILongViewClickListener<Feature>,
        NotifyArrayList.IListener,
        SyncContract.View {

    public static SyncEdFragment newInstance() {
        Bundle args = new Bundle();
        SyncEdFragment fragment = new SyncEdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int longEdit;
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
    private SyncPresenter presenter;

    public SyncEdFragment setTable(FeatureTable table) {
        this.table = table;
        return this;
    }

    public SyncEdFragment setPageSize(int pageSize, int pageNum) {
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
            view = inflater.inflate(R.layout.task_ui_frg_sync_ed, container, false);
            initialize();
            registerEvent();
            initClass();
        }
        return view;
    }

    protected void initialize() {
        presenter = new SyncPresenter();
        presenter.attachView(this);
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
        Short status = (Short) data.getAttributes().get(DataStatus.DATA_STATUS);
        DataStatus dataStatus = DataStatus.getEnum(status);
        if (dataStatus == DataStatus.REMOTE_SYNC) {
            new ATEDialog.Theme_Alert(context)
                    .title("提示")
                    .content("该数据已经提交至服务器，确认继续提交？")
                    .positiveText("提交")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            syncData(data);
                        }
                    }).show();
        }
    }

    @Override
    public void onLongViewClick(View view, Feature data, int position) {
        longEdit = position;
        new ATEDialog.Theme_Alert(context)
                .title("提示")
                .content("确认编辑该条数据？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SingleEditActivity.data = data;
                        Intent intent = new Intent(context, SingleEditActivity.class);
                        startActivityForResult(intent, 1000);
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Feature feature = adapter.getItem(longEdit);
        String uuid = FeatureUtil.getAsT(feature, "UUID");
        MultiCompute.updateFeature(uuid, null, new MultiCompute.ICallBack() {
            @Override
            public void callback(Feature feature) {
                ((DataSyncAtyV1) activity).dispatchData(feature);
            }
        });
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
        if (v.getId() == R.id.tvClear) {
            List<Feature> data = adapter.getSelData();
            if (data == null || data.size() == 0) return;
            new ATEDialog.Theme_Alert(context)
                    .title("提示")
                    .content("确认清空所选中的" + data.size() + "项?")
                    .positiveText("清空")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            adapter.clearAllSel();
                        }
                    }).show();
        }
    }

    private void queryData(int pageNum) {
        QueryParameters params = new QueryParameters();
        params.setReturnGeometry(true);
        params.setWhereClause(DataStatus.SYNC_ED());
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

    public DataSyncAdapter getAdapter() {
        return adapter;
    }

    //-------------------------------------------数据上传-------------------------------------------
    private void syncData(Feature feature) {
        Map<String, Object> map = TransformUtil.feaConvertMap(feature);
        DataSync dataSync = new DataSync();
        dataSync.setGSMM(map);
        dataSync.setUserId(Constant.getUserInfo().getId());
        String fileJson = (String) map.get("GSZP");
        List<File> files = CollectImgBean.obtainFiles(fileJson);
        dataSync.addFile(files);
        presenter.syncSingle(dataSync);
    }


    @Override
    public void syncSuccess(TTResult<Map> result) {
        if (result.getResult()) {
            String success = (String) result.getContent().get("success");
            if (!ObjectUtil.isEmpty(success.trim())) {
                String[] items = success.split(",");
                for (String uuid : items) {
                    MultiCompute.updateFeature(uuid, DataStatus.createSync(), new MultiCompute.ICallBack() {
                        @Override
                        public void callback(Feature feature) {
                            ((DataSyncAtyV1) activity).dispatchData(feature);
                        }
                    });
                }
                showLongToast("数据上传成功");
            } else {
                showLongToast("数据上传失败");
            }
        }
    }
}
