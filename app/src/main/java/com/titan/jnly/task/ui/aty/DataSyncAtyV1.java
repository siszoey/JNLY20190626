package com.titan.jnly.task.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.google.android.material.tabs.TabLayout;
import com.lib.bandaid.arcruntime.util.TransformUtil;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.widget.collect.image.CollectImgBean;
import com.titan.jnly.R;
import com.titan.jnly.common.activity.BaseMvpFrgAty;
import com.titan.jnly.common.fragment.BaseMainFragment;
import com.titan.jnly.system.Constant;
import com.titan.jnly.task.apt.SyncFrgAdapter;
import com.titan.jnly.task.bean.DataSync;
import com.titan.jnly.task.ui.frg.SyncAllFragment;
import com.titan.jnly.task.ui.frg.SyncEdFragment;
import com.titan.jnly.task.ui.frg.SyncUnFragment;
import com.titan.jnly.vector.enums.DataStatus;
import com.titan.jnly.vector.util.MultiCompute;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSyncAtyV1 extends BaseMvpFrgAty<SyncPresenter>
        implements
        BaseMainFragment.OnBackToFirstListener,
        SyncContract.View {

    private int pageSize = 30;
    private int pageNum = 1;
    private FeatureTable table;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SyncFrgAdapter frgAdapter;
    private Fragment[] fragments;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void getTable(FeatureTable table) {
        this.table = table;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new SyncPresenter();
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "数据上传", Gravity.CENTER);
        setContentView(R.layout.task_ui_aty_data_sync_v1);
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

    @Override
    protected void initialize() {
        tabLayout = $(R.id.tabLayout);
        viewPager = $(R.id.viewPager);

    }

    @Override
    protected void registerEvent() {
    }

    @Override
    protected void initClass() {

        fragments = new Fragment[3];
        fragments[0] = SyncEdFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        fragments[1] = SyncUnFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        fragments[2] = SyncAllFragment.newInstance().setTable(table).setPageSize(pageSize, pageNum);
        frgAdapter = new SyncFrgAdapter(getSupportFragmentManager(), fragments, new String[]{"已上传", "未上传", "全部数据"});
        viewPager.setAdapter(frgAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("已上传"));
        tabLayout.addTab(tabLayout.newTab().setText("未上传"));
        tabLayout.addTab(tabLayout.newTab().setText("全部数据"));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();
    }

    @Override
    public void onBackToFirstFragment() {
        finish();
    }

    public void dispatchData(Feature... features) {
        if (features == null || features.length == 0) return;
        SyncEdFragment syncEdFragment = (SyncEdFragment) fragments[0];
        SyncUnFragment syncUnFragment = (SyncUnFragment) fragments[1];
        SyncAllFragment syncAllFragment = (SyncAllFragment) fragments[2];
        Feature feature;
        for (int i = 0; i < features.length; i++) {
            feature = features[i];
            if (feature == null) continue;
            if (DataStatus.isEdit(feature)) {
                syncEdFragment.getAdapter().removeData(feature);
                syncUnFragment.getAdapter().insertUpdate(feature);
            }
            if (DataStatus.isSync(feature)) {
                syncEdFragment.getAdapter().insertUpdate(feature);
                syncUnFragment.getAdapter().removeData(feature);
            }
            syncAllFragment.getAdapter().updateData(feature);
        }
    }

    public void dispatchData(List<Feature> features) {
        Feature[] _features = new Feature[features.size()];
        features.toArray(_features);
        dispatchData(_features);
    }


    /**
     * 批量上传
     */
    private void batchUpLoad() {
        int index = tabLayout.getSelectedTabPosition();
        List<Feature> data;
        if (index == 0) {
            SyncEdFragment syncEdFragment = (SyncEdFragment) fragments[0];
            data = syncEdFragment.getAdapter().getSelData();
        } else if (index == 1) {
            SyncUnFragment syncUnFragment = (SyncUnFragment) fragments[1];
            data = syncUnFragment.getAdapter().getSelData();
        } else {
            SyncAllFragment syncAllFragment = (SyncAllFragment) fragments[2];
            data = syncAllFragment.getAdapter().getSelData();
        }
        if (data == null || data.size() == 0) {
            showLongToast("请选择要上传的数据!");
            return;
        }
        List<Integer> checkAdd = verifyAdd(data);
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
                    for (Feature feature : data) {
                        syncData(feature);
                    }
                }
            }).show();
        }
    }

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
                            dispatchData(feature);
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
