package com.titan.jnly.examine.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.ToolContainer;
import com.lib.bandaid.arcruntime.core.WidgetContainer;
import com.lib.bandaid.arcruntime.tools.ZoomIn;
import com.lib.bandaid.arcruntime.tools.ZoomOut;
import com.lib.bandaid.service.imp.LocService;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.PositionUtil;
import com.lib.bandaid.widget.base.EGravity;
import com.lib.bandaid.widget.drag.CustomDrawerLayout;
import com.titan.jnly.R;
import com.titan.jnly.examine.ui.frame.FrameLayer;
import com.titan.jnly.invest.ui.frame.FrameQuery;
import com.titan.jnly.common.tools.ToolClear;
import com.titan.jnly.common.tools.ToolNavi;
import com.titan.jnly.common.tools.ToolTrack;
import com.titan.jnly.common.tools.ZoomLoc;
import com.titan.jnly.common.tools.ToolQuery;

public class ExamineAty extends BaseMvpCompatAty
        implements PositionUtil.ILocStatus,
        View.OnClickListener {

    CustomDrawerLayout drawerLayout;
    LinearLayout menuRight;
    FrameLayout mainFrame;
    FrameLayer frameLayer;
    FloatingActionButton fabAdd;
    ArcMap arcMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_menu, "调查审核", Gravity.CENTER);
        initMapWidget();
        setContentView(R.layout.exam_ui_aty_main_layout);
        permissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_right_search).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawerLayout.toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    }

    @Override
    protected void initClass() {
        arcMap.setCanOffline(true);
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
    public void onBackPressed() {
        new ATEDialog.Theme_Alert(_context)
                .title("提示")
                .content("确认离开当前模块？")
                .positiveText("退出")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAdd) {
            //presenter.
        }
    }

    void permissions() {
        PositionUtil.reqGps(_context, LocService.class, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PositionUtil.CODE) permissions();
    }

    @Override
    public void agree() {

    }

    @Override
    public void refuse() {
        finish();
    }

    void initMapWidget() {
        ToolContainer.registerTool("通用", EGravity.RIGHT_CENTER, ToolTrack.class, ToolQuery.class, ToolNavi.class, ToolClear.class);//ToolQuery.class,
        ToolContainer.registerTool("辅助", EGravity.LEFT_BOTTOM, ZoomIn.class, ZoomOut.class, ZoomLoc.class);
        WidgetContainer.registerWidget(FrameQuery.class);
    }
}
