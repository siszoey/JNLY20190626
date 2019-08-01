package com.lib.bandaid.arcruntime.core;

import android.location.Location;
import android.os.Handler;
import android.widget.Toast;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.lib.bandaid.utils.CollectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/5/9.
 * 地图操作控制
 */

public class MapControl extends BaseContainer {

    /**
     * 当前用户所在位置
     */
    private Location location;

    private LocationDisplay locationDisplay;

    public void zoomIn() {
        mapView.setViewpointScaleAsync(mapView.getMapScale() * 0.5);
    }

    public void zoomOut() {
        mapView.setViewpointScaleAsync(mapView.getMapScale() * 2);
    }

    public void zoomG(Geometry... geometries) {
        if (geometries == null) return;
        List<Geometry> list = CollectUtil.array2List(geometries);
        if (list.size() == 1) {
            Geometry geometry = GeometryEngine.simplify(list.get(0));
            if (geometry.isEmpty()) return;
            if (geometry instanceof Point) {
                arcMap.getMapView().setViewpoint(new Viewpoint((Point) geometry,2000d));
            } else {
                arcMap.getMapView().setViewpointGeometryAsync(geometry);
            }
        } else {
            Envelope envelope = GeometryEngine.combineExtents(list);
            if (envelope.isEmpty()) return;
            arcMap.getMapView().setViewpointGeometryAsync(envelope);
        }
    }

    public void zoomG(List<Geometry> geometries) {
        if (geometries == null) return;
        Envelope envelope = GeometryEngine.combineExtents(geometries);
        arcMap.getMapView().setViewpoint(new Viewpoint(envelope));
    }

    public void zoomF(Feature... features) {
        if (features == null) return;
        List<Geometry> list = new ArrayList<>();
        for (int i = 0; i < features.length; i++) {
            list.add(features[i].getGeometry());
        }
        zoomG(list);
    }

    public void zoomF(List<Feature> features) {
        if (features == null) return;
        List<Geometry> list = new ArrayList<>();
        for (int i = 0; i < features.size(); i++) {
            list.add(features.get(i).getGeometry());
        }
        zoomG(list);
    }

    public void zoomF(Feature feature) {
        if (feature == null) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                zoomG(feature.getGeometry());
            }
        }, 100);
    }

    public void zoomP(double lon, double lat) {
        Point point = (Point) GeometryEngine.project(new Point(lon, lat), arcMap.getMapView().getSpatialReference());
        if (point == null) return;
        arcMap.getMapView().setViewpoint(new Viewpoint(point, 500));
    }

    public void locCurrent() {
        if (location == null)
            Toast.makeText(context, "未能获取位置信息,请确认位置服务是否开启!", Toast.LENGTH_LONG).show();
        Point gpsPoint = new Point(location.getLongitude(), location.getLatitude(), SpatialReferences.getWgs84());
        Point point = (Point) GeometryEngine.project(gpsPoint, arcMap.getMapView().getSpatialReference());
        if (point == null) return;
        arcMap.getMapView().setViewpoint(new Viewpoint(point, 5000));
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public MapControl initDefaultLocation() {
        locationDisplay = arcMap.getMapView().getLocationDisplay();
        //设置定位模式
        locationDisplay.setNavigationPointHeightFactor(0.5f);
        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        //locationDisplay.startAsync();
        return this;
    }

    public void useDefaultLocation(long delay) {
        new Handler().postDelayed(new Runnable() {

            public void run() {
                if (locationDisplay != null) locationDisplay.startAsync();
            }
        }, delay);
    }
}
