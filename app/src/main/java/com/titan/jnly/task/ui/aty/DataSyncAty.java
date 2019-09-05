package com.titan.jnly.task.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.arcruntime.util.FeatureUtil;
import com.lib.bandaid.arcruntime.util.TransformUtil;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.NotifyArrayList;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.widget.collect.image.CollectImgBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.titan.jnly.R;
import com.titan.jnly.system.Constant;
import com.titan.jnly.task.apt.DataSyncAdapter;
import com.titan.jnly.task.bean.DataSync;
import com.titan.jnly.vector.enums.DataStatus;
import com.titan.jnly.vector.ui.aty.SingleEditActivity;
import com.titan.jnly.vector.util.MultiCompute;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataSyncAty extends BaseMvpCompatAty<SyncPresenter>
        implements
        SyncContract.View,
        OnRefreshListener,
        OnLoadMoreListener,
        BaseRecycleAdapter.IViewClickListener<Feature>,
        View.OnClickListener,
        NotifyArrayList.IListener,
        BaseRecycleAdapter.ILongViewClickListener<Feature> {

    private final int pageSize = 30;
    private int pageNum = 1;

    private String title = "数据上传";
    private TextView tvCount, tvClear;
    private SmartRefreshLayout swipeLayout;
    private RecyclerView rvList;
    private FeatureTable table;
    private DataSyncAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new SyncPresenter();
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, title, Gravity.CENTER);
        setContentView(R.layout.task_ui_aty_data_sync);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_right_up_load).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_right_up_load) {
            batchUpLoad();
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void getTable(FeatureTable table) {
        this.table = table;
        this.title = table.getTableName() + title;
    }

    @Override
    protected void initialize() {
        swipeLayout = $(R.id.swipeLayout);
        rvList = $(R.id.rvList);
        tvCount = $(R.id.tvCount);
        tvClear = $(R.id.tvClear);
    }

    @Override
    protected void registerEvent() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadMoreListener(this);
        tvClear.setOnClickListener(this);
    }

    @Override
    protected void initClass() {
        adapter = new DataSyncAdapter(rvList, this);
        adapter.setIViewClickListener(this);
        adapter.setILongViewClickListener(this);
        queryData(pageNum);
    }

    private void queryData(int pageNum) {
        QueryParameters params = new QueryParameters();
        params.setReturnGeometry(true);
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
    public void onClick(View view, Feature data, int position) {
        if (view.getId() == R.id.ivSync) {
            Short status = (Short) data.getAttributes().get(DataStatus.DATA_STATUS);
            DataStatus dataStatus = DataStatus.getEnum(status);
            if (dataStatus == DataStatus.LOCAL_ADD) {
                new ATEDialog.Theme_Alert(_context)
                        .title("提示")
                        .content("该条数据尚未填写完成，前去完善？")
                        .positiveText("完善")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                EventBus.getDefault().post(data);
                                finish();
                            }
                        }).show();
            }
            if (dataStatus == DataStatus.LOCAL_EDIT) {
                new ATEDialog.Theme_Alert(_context)
                        .title("提示")
                        .content("确认提交数据？")
                        .positiveText("提交")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                syncData(data);
                            }
                        }).show();
            }
            if (dataStatus == DataStatus.REMOTE_SYNC) {
                new ATEDialog.Theme_Alert(_context)
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
    }

    private void syncData(Feature feature) {
        //单个上传
        Map<String, Object> map = TransformUtil.feaConvertMap(feature);
        DataSync dataSync = new DataSync();
        dataSync.setGSMM(map);
        dataSync.setUserId(Constant.getUserInfo().getId());

        String fileJson = (String) map.get("GSZP");
        List<File> files = CollectImgBean.obtainFiles(fileJson);
        dataSync.addFile(files);
        presenter.syncSingle(dataSync);
    }

    /**
     * 批量上传
     */
    private void batchUpLoad() {
        List<Feature> data = adapter.getSelData();
        if (data == null || data.size() == 0) {
            showLongToast("请选择要上传的数据!");
            return;
        }
        List<Integer> checkAdd = verifyAdd(data);
        //List<Integer> checkSync = verifyAdd(data);
        if (checkAdd.size() > 0) {
            new ATEDialog.Theme_Alert(_context)
                    .title("提示")
                    .content("提交的数据中，有" + checkAdd.size() + "个尚未完成调查，请检查!")
                    .positiveText("确认").show();
        } else {
            new ATEDialog.Theme_Alert(_context)
                    .title("提示")
                    .content("确认上传这" + data.size() + "条数据？")
                    .positiveText("确认")
                    .negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    //showToast("开始上传");
                    for(Feature feature:data){
                        syncData(feature);
                    }
                }
            }).show();
        }
    }

    private List<Integer> verifyAdd(List<Feature> data) {
        List<Integer> check = new ArrayList<>();
        Short status;
        DataStatus dataStatus;
        Feature feature;
        for (int i = 0; i < data.size(); i++) {
            feature = data.get(i);
            status = (Short) feature.getAttributes().get(DataStatus.DATA_STATUS);
            dataStatus = DataStatus.getEnum(status);
            if (dataStatus == DataStatus.LOCAL_ADD) {
                check.add(i);
            }
        }
        return check;
    }

    private List<Integer> verifySync(List<Feature> data) {
        List<Integer> check = new ArrayList<>();
        Short status;
        DataStatus dataStatus;
        Feature feature;
        for (int i = 0; i < data.size(); i++) {
            feature = data.get(i);
            status = (Short) feature.getAttributes().get(DataStatus.DATA_STATUS);
            dataStatus = DataStatus.getEnum(status);
            if (dataStatus == DataStatus.REMOTE_SYNC) {
                check.add(i);
            }
        }
        return check;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvClear) {
            List<Feature> data = adapter.getSelData();
            if (data == null || data.size() == 0) return;
            new ATEDialog.Theme_Alert(_context)
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

    @Override
    public void itemChange() {
        tvCount.setText(adapter.getSelCount() + "");
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
                            adapter.updateData(feature);
                        }
                    });
                }
                showLongToast("数据上传成功");
            } else {
                showLongToast("数据上传失败");
            }
        }

    }

    int longEdit;

    @Override
    public void onLongViewClick(View view, Feature data, int position) {
        longEdit = position;
        new ATEDialog.Theme_Alert(_context)
                .title("提示")
                .content("确认编辑该条数据？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        SingleEditActivity.data = data;
                        Intent intent = new Intent(_context, SingleEditActivity.class);
                        startActivityForResult(intent, 1000);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Feature feature = adapter.getItem(longEdit);
        String uuid = FeatureUtil.getAsT(feature, "UUID");
        MultiCompute.updateFeature(uuid, null, new MultiCompute.ICallBack() {
            @Override
            public void callback(Feature feature) {
                adapter.updateData(feature);
            }
        });
    }
}
