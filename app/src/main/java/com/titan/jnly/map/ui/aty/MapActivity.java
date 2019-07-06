package com.titan.jnly.map.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.ToolContainer;
import com.lib.bandaid.arcruntime.core.WidgetContainer;
import com.lib.bandaid.arcruntime.tools.ZoomIn;
import com.lib.bandaid.arcruntime.tools.ZoomOut;
import com.lib.bandaid.widget.base.EGravity;
import com.lib.bandaid.widget.drag.CustomDrawerLayout;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.frame.FrameLayer;
import com.titan.jnly.map.ui.frame.FrameQuery;
import com.titan.jnly.map.ui.tools.QuerySel;
import com.titan.jnly.map.ui.tools.ToolClear;
import com.titan.jnly.map.ui.tools.ToolLocal;
import com.titan.jnly.map.ui.tools.ToolNavi;
import com.titan.jnly.map.ui.tools.ToolQuery;
import com.titan.jnly.map.ui.tools.ToolSel;
import com.titan.jnly.map.ui.tools.ToolTrack;

public class MapActivity extends BaseAppCompatActivity implements ArcMap.IMapReady {


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
        drawerLayout.setMargin(0.618f);
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


    @Override
    public void onMapReady() {
    }

    public void toggle() {
        drawerLayout.toggle();
    }

    void initMapWidget() {
        ToolContainer.registerTool("通用", EGravity.RIGHT_CENTER, ToolTrack.class, ToolNavi.class, ToolQuery.class, ToolLocal.class, ToolClear.class);
        ToolContainer.registerTool("辅助", EGravity.RIGHT_BOTTOM, ZoomIn.class, ZoomOut.class);
        WidgetContainer.registerWidget(FrameQuery.class);
    }
}