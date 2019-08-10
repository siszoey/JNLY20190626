package com.titan.jnly.map.ui.aty;

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
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.ToolContainer;
import com.lib.bandaid.arcruntime.core.WidgetContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.tools.ZoomIn;
import com.lib.bandaid.arcruntime.tools.ZoomOut;
import com.lib.bandaid.service.imp.LocService;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.PositionUtil;
import com.lib.bandaid.utils.ToastUtil;
import com.lib.bandaid.widget.base.EGravity;
import com.lib.bandaid.widget.drag.CustomDrawerLayout;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.frame.FrameLayer;
import com.titan.jnly.map.ui.frame.FrameQuery;
import com.titan.jnly.map.ui.frame.VectorBar;
import com.titan.jnly.map.ui.tools.ToolClear;
import com.titan.jnly.map.ui.tools.ToolEdit;
import com.titan.jnly.map.ui.tools.ToolNavi;
import com.titan.jnly.map.ui.tools.ToolTrack;
import com.titan.jnly.map.ui.tools.ZoomLoc;
import com.titan.jnly.system.version.bugly.BuglySetting;
import com.titan.jnly.task.ui.aty.DataSyncAty;
import com.titan.jnly.vector.ui.aty.SingleEditActivityV1;
import com.titan.jnly.vector.util.MultiCompute;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MapActivity extends BaseAppCompatActivity implements PositionUtil.ILocStatus, View.OnClickListener {


    CustomDrawerLayout drawerLayout;
    LinearLayout menuRight;
    FrameLayout mainFrame;
    FrameLayer frameLayer;
    FloatingActionButton fabAdd;
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
                    startActivity(new Intent(_context, DataSyncAty.class));
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
            MultiCompute.getLastFeature(new MultiCompute.ICallBack() {
                @Override
                public void callback(Feature feature) {
                   //Long id = (Long) feature.getAttributes().get("OBJECTID");
                    // System.out.println(feature);
                    Intent intent = new Intent(_context, SingleEditActivityV1.class);
                    startActivity(intent);

                }
            });
        }
    }
}