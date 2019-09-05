package com.lib.bandaid.arcruntime.util;

import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.ImmutablePart;
import com.esri.arcgisruntime.geometry.ImmutablePartCollection;
import com.esri.arcgisruntime.geometry.ImmutablePointCollection;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.util.DateUtil;
import com.lib.bandaid.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2018/1/5.
 */

public class TransformUtil {

    public static Map<String, Object> feaConvertMap(@NonNull Feature feature) {
        Map<String, Object> map = new HashMap<>();
        String wkt = getWkt(feature.getGeometry());
        map.put("SHAPE", wkt);
        Map<String, Object> property = feature.getAttributes();
        Object val;
        for (String key : property.keySet()) {
            val = property.get(key);
            if (ObjectUtil.isDate(val)) {
                val = DateUtil.convert2DateStr(val);
            }
            map.put(key, val);
        }
        return map;
    }

    public static String getWkt(Geometry geometry) {
        String wkt = null;
        if (geometry != null) {
            GeometryType geometryType = geometry.getGeometryType();
            if (geometryType == GeometryType.POINT) {
                wkt = getPOINT2Wkt(geometry);
            }
            if (geometryType == GeometryType.MULTIPOINT) {
                wkt = getMULTIPOINT2Wkt(geometry);
            }
            if (geometryType == GeometryType.POLYGON) {
                wkt = getPOLYGON2Wkt(geometry);
            }
            if (geometryType == GeometryType.ENVELOPE) {
                wkt = getENVELOPE2Wkt(geometry);
            }
            if (geometryType == GeometryType.POLYLINE) {
                wkt = getPOLYLINE2Wkt(geometry);
            }
            if (geometryType == GeometryType.UNKNOWN) {
                wkt = geometry.toString();
            }
        }
        return wkt;
    }

    public static String getPOINT2Wkt(Geometry geometry) {
        String wkt = "";
        Point point = (Point) geometry;
        wkt = "POINT (" + point.getX() + " " + point.getY() + ")";
        return wkt;
    }

    public static String getMULTIPOINT2Wkt(Geometry geometry) {
        String wkt = "";
        StringBuilder sb = new StringBuilder("MULTIPOINT");
        Multipoint multipoint = (Multipoint) geometry;
        ImmutablePointCollection points = multipoint.getPoints();
        Point point;
        for (int i = 0; points != null && i < points.size(); i++) {
            if (i == 0) {
                sb.append("(");
            }
            point = points.get(i);
            sb.append(point.getX() + " " + point.getY());
            if (i != points.size() - 1) {
                sb.append(",");
            } else {
                sb.append(")");
            }
        }
        wkt = sb.toString();
        return wkt;
    }

    public static String getPOLYGON2Wkt(Geometry geometry) {
        String wkt = "";
        StringBuilder sb = new StringBuilder("POLYGON (");
        Polygon polygon = (Polygon) geometry;
        ImmutablePartCollection partCollection = polygon.getParts();
        int partSize = partCollection.size();
        ImmutablePart immutablePart;
        Iterator<Point> iterator;
        Point point;
        for (int i = 0; i < partSize; i++) {
            sb.append("(");
            immutablePart = partCollection.get(i);
            iterator = immutablePart.getPoints().iterator();
            boolean isStart = true;
            String startStr = "";
            while (iterator.hasNext()) {
                point = iterator.next();
                if (isStart) {
                    startStr = point.getX() + " " + point.getY();
                    isStart = false;
                }
                sb.append(point.getX() + " " + point.getY()).append(",");
            }
            sb.append(startStr);
            sb.append(")");
            if (i != partSize - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        wkt = sb.toString();
        return wkt;
    }

    public static String getPOLYLINE2Wkt(Geometry geometry) {
        String wkt = "";
        StringBuilder sb;
        Polyline polyline = (Polyline) geometry;
        ImmutablePartCollection partCollection = polyline.getParts();
        int partSize = partCollection.size();
        if (partSize == 1) {
            sb = new StringBuilder("LINESTRING (");
        } else {
            sb = new StringBuilder("MULTILINESTRING (");
        }
        ImmutablePart immutablePart;
        Iterator<Point> iterator;
        Point point;
        for (int i = 0; i < partSize; i++) {
            sb.append("(");
            immutablePart = partCollection.get(i);
            iterator = immutablePart.getPoints().iterator();
            while (iterator.hasNext()) {
                point = iterator.next();
                sb.append(point.getX() + " " + point.getY()).append(",");
            }
            sb.substring(0, sb.length() - 1);
            sb.append(")");
            if (i != partSize - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        wkt = sb.toString();
        return wkt;
    }

    public static String getENVELOPE2Wkt(Geometry geometry) {
        StringBuilder sb = new StringBuilder("ENVELOPE");
        Envelope env = (Envelope) geometry;
        sb.append("(").append(env.getXMin()).append(",").append(env.getYMin()).append(",").append(env.getXMax()).append(",").append(env.getYMax()).append(")");
        return sb.toString();
    }


    public static Envelope mergeEnvelope(List<Envelope> envelopes) {
        Envelope envelope = null;
        if (envelopes != null && envelopes.size() > 0) {
            double tempMinX = Double.MAX_VALUE, tempMaxX = Double.MIN_VALUE, tempMinY = Double.MAX_VALUE, tempMaxY = Double.MIN_VALUE;
            Envelope e1 = envelopes.get(0);
            if (e1 == null) return null;
            SpatialReference sr = e1.getSpatialReference();
            for (Envelope e : envelopes) {
                if (e.getXMin() < tempMinX) {
                    tempMinX = e.getXMin();
                }
                if (e.getXMax() > tempMaxX) {
                    tempMaxX = e.getXMax();
                }

                if (e.getYMin() < tempMinY) {
                    tempMinY = e.getYMin();
                }
                if (e.getYMax() > tempMaxY) {
                    tempMaxY = e.getYMax();
                }
            }
            envelope = new Envelope(tempMinX, tempMinY, tempMaxX, tempMaxY, sr);
        }
        return envelope;
    }

    public static String esriFeature2GeoJson(Feature feature) {
       /* String wkt = getWkt(feature.getGeometry());
        Map<String, Object> attributes = feature.getAttributes();

        WKBReader wkbReader = new WKBReader();
        GeometryJSON gJson = new GeometryJSON(12);

        JSONObject geoJson = new JSONObject();
        JSONObject properties = new JSONObject();
        geoJson.put("geometry", new JSONObject(wkt));

        for (String key : attributes.keySet()) {
            if (key.toLowerCase().contains("shape") || key.toString().toLowerCase().contains("geom")) {
                feature.put("geometry", new JSONObject(geoJson));
                continue;
            }
            val = property.get(key);
            if (val == null) {
                properties.put(key, JSONObject.NULL);
                continue;
            }
            if (val instanceof String) {
                properties.put(key.toString(), val.toString());
            }
            if (val instanceof Number) {
                properties.put(key.toString(), val);
            }*/
        return null;
    }


    /**
     * @param arcMap
     * @param circleX 屏幕坐标
     * @param circleY 屏幕坐标
     * @param radius  距离 屏幕坐标
     * @param steps
     * @return
     */
    public static Geometry circle2Polygon(ArcMap arcMap, float circleX, float circleY, float radius, int steps) {
        List<Point> coordinates = destination(arcMap, circleX, circleY, radius, steps);
        PointCollection collection = new PointCollection(coordinates);
        Polygon polygon = new Polygon(collection, arcMap.getMapView().getSpatialReference());
        return polygon;
    }


    public static Polygon path2Polygon(ArcMap arcMap, Path path) {
        return null;
    }

    public static RectF getPathEnvelope(ArcMap arcMap, Path path) {
        return null;
    }


    /**
     * @param arcMap
     * @param circleX
     * @param circleY
     * @param radius
     * @param steps   分割份数
     * @return
     */
    public static List<Point> destination(ArcMap arcMap, float circleX, float circleY, float radius, int steps) {
        List<Point> positions = new ArrayList<>();
        Point temp;
        for (int i = 0; i < steps; i++) {
            double x = circleX + Math.cos(360 * i / steps) * radius;
            double y = circleY + Math.sin(360 * i / steps) * radius;
            temp = arcMap.screenToLocation((int) x, (int) y);
            positions.add(temp);
        }
        positions.add(positions.get(0));
        return positions;
    }
}
