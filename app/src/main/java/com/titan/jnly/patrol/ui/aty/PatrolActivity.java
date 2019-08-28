package com.titan.jnly.patrol.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.ToolContainer;
import com.lib.bandaid.arcruntime.core.WidgetContainer;
import com.lib.bandaid.arcruntime.tools.ZoomIn;
import com.lib.bandaid.arcruntime.tools.ZoomOut;
import com.lib.bandaid.utils.PositionUtil;
import com.lib.bandaid.widget.base.EGravity;
import com.lib.bandaid.widget.drag.CustomDrawerLayout;
import com.titan.jnly.R;
import com.titan.jnly.invest.ui.frame.FrameQuery;
import com.titan.jnly.invest.ui.tools.ToolClear;
import com.titan.jnly.invest.ui.tools.ToolNavi;
import com.titan.jnly.invest.ui.tools.ToolQuery;
import com.titan.jnly.invest.ui.tools.ToolTrack;
import com.titan.jnly.invest.ui.tools.ZoomLoc;
import com.titan.jnly.patrol.ui.frame.FrameLayer;

public class PatrolActivity extends BaseMvpCompatAty
        implements PositionUtil.ILocStatus, View.OnClickListener {


    CustomDrawerLayout drawerLayout;
    LinearLayout menuRight;
    FrameLayout mainFrame;
    FrameLayer frameLayer;
    FloatingActionButton fabAdd;
    ArcMap arcMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_menu, "古树巡查", Gravity.CENTER);
        initMapWidget();
        setContentView(R.layout.map_ui_aty_patrol);
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
    public void onClick(View v) {

    }

    @Override
    public void agree() {

    }

    @Override
    public void refuse() {

    }

    void initMapWidget() {
        ToolContainer.registerTool("通用", EGravity.RIGHT_CENTER, ToolTrack.class, ToolQuery.class, ToolNavi.class, ToolClear.class);//ToolQuery.class,
        ToolContainer.registerTool("辅助", EGravity.LEFT_BOTTOM, ZoomIn.class, ZoomOut.class, ZoomLoc.class);
        WidgetContainer.registerWidget(FrameQuery.class);
    }
}
