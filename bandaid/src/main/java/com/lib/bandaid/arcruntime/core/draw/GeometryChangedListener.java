package com.lib.bandaid.arcruntime.core.draw;

import android.annotation.SuppressLint;
import android.view.View;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchGeometryChangedEvent;
import com.esri.arcgisruntime.mapping.view.SketchGeometryChangedListener;


public class GeometryChangedListener implements SketchGeometryChangedListener {

    private MapView mapView;
    private ValueCallback callBack;

    public GeometryChangedListener(MapView mapView, ValueCallback callBack) {
        this.mapView = mapView;
        this.callBack = callBack;
    }

    @Override
    public void geometryChanged(SketchGeometryChangedEvent event) {
        boolean b = event.getSource().isSketchValid();
        Geometry geometry = event.getGeometry();
        if (b) {
            callBack.onGeometry(geometry);
        } else {
            //setListener();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        View.OnTouchListener touch = mapView.getOnTouchListener();
        if (touch instanceof SketchDrawTouchEvent) {
            return;
        }

        //添加地图的Touch监听
        DefaultMapViewOnTouchListener lo = (DefaultMapViewOnTouchListener) mapView.getOnTouchListener();
        SketchDrawTouchEvent sketchDrawTouchEvent = new SketchDrawTouchEvent(mapView, lo, callBack);
        mapView.setOnTouchListener(sketchDrawTouchEvent);
    }
}
