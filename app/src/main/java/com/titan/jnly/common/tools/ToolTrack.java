package com.titan.jnly.common.tools;

import android.view.View;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.lib.bandaid.service.bean.Loc;
import com.lib.bandaid.util.MathUtil;
import com.lib.bandaid.util.ToastUtil;
import com.titan.jnly.R;
import com.titan.jnly.common.dialog.TrackDialog;

import java.util.ArrayList;
import java.util.List;

public class ToolTrack extends BaseTool {

    public ToolTrack() {
        id = getClass().getSimpleName();
        name = "轨迹";
        /*resId = R.drawable.ic_map_track;
        checkedResId = R.drawable.ic_tool_pressed;*/
        resId = R.mipmap.ic_map_track_normal;
        checkedResId = R.mipmap.ic_map_track_pressed;
    }

    @Override
    public void viewClick(View view) {
        super.viewClick(view);
        TrackDialog.newInstance().setCallBack(new TrackDialog.ICallBack() {
            @Override
            public void sure(List<Loc> res) {
                createTrack(res);
            }
        }).show(context);
    }

    private void createTrack(List<Loc> res) {
        if (res == null) return;
        Loc loc;
        Point point;
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < res.size(); i++) {
            loc = res.get(i);
            Double x = MathUtil.try2Double(loc.getLng());
            Double y = MathUtil.try2Double(loc.getLat());
            if (x == null || y == null) continue;
            point = new Point(x, y);
            point = (Point) GeometryEngine.project(point, arcMap.getMapView().getSpatialReference());
            list.add(point);
        }
        PointCollection collection = new PointCollection(list);
        Polyline polyline = new Polyline(collection);
        //polyline = (Polyline) GeometryEngine.simplify(polyline);
        if (polyline.isEmpty()) {
            ToastUtil.showLong(context, "无法构成有意义的轨迹");
            return;
        }
        arcMap.getGraphicContainer().add(polyline);
        arcMap.getMapControl().zoomG(polyline);
    }
}
