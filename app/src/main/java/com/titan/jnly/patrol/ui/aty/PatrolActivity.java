package com.titan.jnly.patrol.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.ToolContainer;
import com.lib.bandaid.arcruntime.core.WidgetContainer;
import com.lib.bandaid.arcruntime.tools.ZoomIn;
import com.lib.bandaid.arcruntime.tools.ZoomOut;
import com.lib.bandaid.widget.base.EGravity;
import com.titan.jnly.R;
import com.titan.jnly.invest.ui.frame.FrameQuery;
import com.titan.jnly.invest.ui.tools.ToolClear;
import com.titan.jnly.invest.ui.tools.ToolNavi;
import com.titan.jnly.invest.ui.tools.ToolQuery;
import com.titan.jnly.invest.ui.tools.ToolTrack;
import com.titan.jnly.invest.ui.tools.ZoomLoc;

public class PatrolActivity extends BaseMvpCompatAty {

    private ArcMap arcMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, "古树巡查", Gravity.CENTER);
        initMapWidget();
        setContentView(R.layout.map_ui_aty_patrol);
    }

    @Override
    protected void initialize() {
        arcMap = $(R.id.arcMap);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        arcMap.setCanOffline(true);
        arcMap.setMapServerUrl(
                "http://119.164.253.207:6080/arcgis/rest/services/JNGSMM/JNGSMM_2019/MapServer",
                "http://119.164.253.207:6080/arcgis/rest/services/JNGSMM/JNGSQ_2019/MapServer"
        );
        arcMap.mapLoad(new ArcMap.IMapReady() {
            @Override
            public void onMapReady() {

            }
        });
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


    void initMapWidget() {
        ToolContainer.registerTool("通用", EGravity.RIGHT_CENTER, ToolTrack.class, ToolQuery.class, ToolNavi.class, ToolClear.class);//ToolQuery.class,
        ToolContainer.registerTool("辅助", EGravity.LEFT_BOTTOM, ZoomIn.class, ZoomOut.class, ZoomLoc.class);
        WidgetContainer.registerWidget(FrameQuery.class);
    }
}
