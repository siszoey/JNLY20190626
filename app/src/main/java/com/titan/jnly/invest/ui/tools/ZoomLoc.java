package com.titan.jnly.invest.ui.tools;

import android.view.View;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.lib.bandaid.R;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.lib.bandaid.arcruntime.tools.core.ToolView;
import com.titan.jnly.invest.ui.dialog.LocalDialog;

public class ZoomLoc extends BaseTool implements ToolView.ILongClick {

    public ZoomLoc() {
        id = getClass().getSimpleName();
        name = "当前位置";
        resId = R.mipmap.tool_map_zoom_loc_normal;
        checkedResId = R.mipmap.tool_map_zoom_loc_pressed;
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        view.setILongClick(this);
    }

    @Override
    public void viewClick(View view) {
        arcMap.getMapControl().initDefaultLocation().useDefaultLocation(100);
    }

    @Override
    public void viewLongClick(View view) {
        LocalDialog.newInstance(new LocalDialog.ICallBack() {
            @Override
            public void sure(double dLon, double dLat) {
                Point point = new Point(dLon, dLat);
                point = (Point) GeometryEngine.project(point, SpatialReference.create(4326));
                arcMap.getMapControl().zoomG(point);
            }
        }).show(context);
    }
}

