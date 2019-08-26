package com.titan.jnly.invest.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.ToolContainer;
import com.lib.bandaid.arcruntime.core.WidgetContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.tools.ZoomIn;
import com.lib.bandaid.arcruntime.tools.ZoomOut;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.data.remote.core.DownloadManager;
import com.lib.bandaid.data.remote.entity.DownloadInfo;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.service.imp.LocService;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.ClickUtil;
import com.lib.bandaid.utils.CodeUtil;
import com.lib.bandaid.utils.PositionUtil;
import com.lib.bandaid.utils.ToastUtil;
import com.lib.bandaid.widget.base.EGravity;
import com.lib.bandaid.widget.drag.CustomDrawerLayout;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.invest.ui.frame.FrameLayer;
import com.titan.jnly.invest.ui.frame.FrameQuery;
import com.titan.jnly.invest.ui.frame.VectorBar;
import com.titan.jnly.invest.ui.tools.ToolClear;
import com.titan.jnly.invest.ui.tools.ToolEdit;
import com.titan.jnly.invest.ui.tools.ToolNavi;
import com.titan.jnly.invest.ui.tools.ToolTrack;
import com.titan.jnly.invest.ui.tools.ZoomLoc;
import com.titan.jnly.system.Constant;
import com.titan.jnly.system.version.bugly.BuglySetting;
import com.titan.jnly.task.ui.aty.DataSyncAtyV1;
import com.titan.jnly.vector.ui.aty.SimpleAddAty;
import com.titan.jnly.vector.util.MultiCompute;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Date;

public class InvestActivity
        extends BaseMvpCompatAty<InvestAtyPresenter>
        implements InvestAtyContract.View,
        PositionUtil.ILocStatus,
        View.OnClickListener {


    CustomDrawerLayout drawerLayout;
    LinearLayout menuRight;
    FrameLayout mainFrame;
    FrameLayer frameLayer;
    FloatingActionButton fabAdd;
    ArcMap arcMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = new InvestAtyPresenter();
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_menu, "济南名木", Gravity.CENTER);
        initMapWidget();
        setContentView(R.layout.map_ui_aty_invest);
        //检查更新
        BuglySetting.checkVersion();
        //权限
        permissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_right_up_load_group).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            toggle();
            return true;
        }
        if (id == R.id.menu_right_up_load_group) {
            LayerNode layerNode = arcMap.getTocContainer().getLayerNodeByName("古树名木单株调查");
            if (layerNode != null) {
                FeatureTable table = layerNode.tryGetFeaTable();
                if (table != null) {
                    EventBus.getDefault().postSticky(table);
                    //startActivity(new Intent(_context, DataSyncAty.class));
                    startActivity(new Intent(_context, DataSyncAtyV1.class));
                } else {
                    ToastUtil.showLong(_context, "未能找到要上传的数据！");
                }
            } else {
                showLongToast("地图尚未加载完成，请稍等！");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        arcMap.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        arcMap.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        arcMap.destroy();
    }

    @Override
    protected void initialize() {
        arcMap = $(R.id.arcMap);
        fabAdd = $(R.id.fabAdd);
        drawerLayout = $(R.id.drawerLayout);
        drawerLayout.setMargin(0.5f);
        menuRight = $(R.id.menuRight);
        mainFrame = $(R.id.mainFrame);

        frameLayer = new FrameLayer(this);
        frameLayer.create(arcMap);
        menuRight.addView(frameLayer.getView());
    }

    @Override
    protected void registerEvent() {
        fabAdd.setOnClickListener(this);
    }

    @Override
    protected void initClass() {
    }


    public void toggle() {
        drawerLayout.toggle();
    }

    void initMapWidget() {
        ToolContainer.registerTool("通用", EGravity.RIGHT_CENTER, ToolTrack.class, ToolNavi.class, ToolEdit.class, ToolClear.class);//ToolQuery.class,
        ToolContainer.registerTool("辅助", EGravity.LEFT_BOTTOM, ZoomIn.class, ZoomOut.class, ZoomLoc.class);
        WidgetContainer.registerWidget(FrameQuery.class);
        WidgetContainer.registerWidget(VectorBar.class);
    }

    void permissions() {
        PositionUtil.reqGps(_context, LocService.class, this);
    }

    @Override
    public void agree() {

    }

    @Override
    public void refuse() {
        finish();
    }

    @Override
    public void onBackPressed() {
        new ATEDialog.Theme_Alert(_context)
                .title("提示")
                .content("确认退出？")
                .positiveText("退出")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                }).show();
    }


    /**
     * 完善数据
     *
     * @param feature
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fillData(Feature feature) {
        arcMap.getMapControl().zoomF(feature);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAdd) {
            if (ClickUtil.isFastDoubleClick(R.id.fabAdd)) return;
            LayerNode layerNode = arcMap.getTocContainer().getLayerNodeByName("古树名木单株调查");
            if (layerNode == null) return;
            FeatureTable single = layerNode.tryGetFeaTable();
            MultiCompute.getLastFeature(new MultiCompute.ICallBack() {
                @Override
                public void callback(Feature feature) {
                    Intent intent = new Intent(_context, SimpleAddAty.class);
                    EventBus.getDefault().postSticky(new Object[]{single, feature});
                    startActivity(intent);
                }
            });
        }
    }

    public void reqInfo() {
        presenter.requestInfo(Constant.getUser());
    }

    @Override
    public void reqSuccess(UserInfo info) {
        //更新账户及权限
        if (info != null) {
            if (!info.localCheck()) info.setLastLogin(new Date());
            String md5 = CodeUtil.convertMd5(Constant.getUser().getPwd());
            info.setPwd(md5);
            DbManager.createDefault().saveOrUpdate(info);
            Constant.putUserInfo(info);
            showLongToast("账号权限更新成功!");
        }
        showLoading();
        //更新字典
        String path = Config.APP_PATH_DIC.concat(File.separator).concat("dic_new.db");
        FileUtil.deleteFile(path);
        DownloadInfo down = new DownloadInfo(Config.BASE_URL.Sync_Dic, path);
        DownloadManager.getInstance().download(down);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dic(DownloadInfo info) {
        if (info.isStart()) {
            showLoading();
        }
        if (info.isComplete()) {
            String old = Config.APP_DIC_DB_PATH;
            boolean del = FileUtil.deleteFile(old);
            if (del) del = FileUtil.rename(info.getFilePath(), old);
            if (del) showLongToast("字典库更新成功");
            else showLongToast("字典库更新失败");
            Constant.reloadSpecies();
            hideLoading();
        }
        if (info.overButUnComplete()) {
            showLongToast("字典库更新失败");
            hideLoading();
        }
    }
}