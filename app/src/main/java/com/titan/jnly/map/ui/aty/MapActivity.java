package com.titan.jnly.map.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.ToolContainer;
import com.lib.bandaid.arcruntime.core.WidgetContainer;
import com.lib.bandaid.arcruntime.tools.ZoomIn;
import com.lib.bandaid.arcruntime.tools.ZoomOut;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.permission.Permission;
import com.lib.bandaid.permission.RxConsumer;
import com.lib.bandaid.permission.RxPermissionFactory;
import com.lib.bandaid.permission.SimplePermission;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.service.imp.LocService;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.PositionUtil;
import com.lib.bandaid.widget.base.EGravity;
import com.lib.bandaid.widget.drag.CustomDrawerLayout;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.frame.FrameLayer;
import com.titan.jnly.map.ui.frame.FrameQuery;
import com.titan.jnly.map.ui.frame.VectorBar;
import com.titan.jnly.map.ui.tools.ToolClear;
import com.titan.jnly.map.ui.tools.ToolEdit;
import com.titan.jnly.map.ui.tools.ToolLocal;
import com.titan.jnly.map.ui.tools.ToolNavi;
import com.titan.jnly.map.ui.tools.ToolQuery;
import com.titan.jnly.map.ui.tools.ToolTrack;
import com.titan.jnly.map.ui.tools.ZoomLoc;
import com.titan.jnly.system.version.bugly.BuglySetting;
import com.titan.jnly.vector.bean.TreeMode;

import java.util.List;

public class MapActivity extends BaseAppCompatActivity implements  PositionUtil.ILocStatus {


    CustomDrawerLayout drawerLayout;
    LinearLayout menuRight;
    FrameLayout mainFrame;
    FrameLayer frameLayer;
    ArcMap arcMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_menu, "济南名木", Gravity.CENTER);
        initMapWidget();
        setContentView(R.layout.map_ui_aty_map);
        //检查更新
        BuglySetting.checkVersion();
        //权限
        permissions();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initialize() {
        arcMap = $(R.id.arcMap);
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

    }

    @Override
    protected void initClass() {
    }


    public void toggle() {
        drawerLayout.toggle();
    }

    void initMapWidget() {
        ToolContainer.registerTool("通用", EGravity.RIGHT_CENTER, ToolTrack.class, ToolNavi.class, ToolQuery.class, ToolEdit.class, ToolClear.class);
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
}