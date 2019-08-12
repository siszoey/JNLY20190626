package com.lib.bandaid.arcruntime.util;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryBuilder;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.ImmutablePart;
import com.esri.arcgisruntime.geometry.ImmutablePartCollection;
import com.esri.arcgisruntime.geometry.ImmutablePointCollection;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.lib.bandaid.utils.DecimalFormats;

import org.json.JSONObject;

import java.math.BigDecimal;
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
     * 将小数度数转换为度分秒格式
     *
     * @param latlng
     * @return
     */
    public static String _10To60(String latlng) {
        double num = Double.parseDouble(latlng);
        int du = (int) Math.floor(Math.abs(num));    //获取整数部分
        double temp = getdPoint(Math.abs(num)) * 60;
        int fen = (int) Math.floor(temp); //获取整数部分
        int miao = (int) (getdPoint(temp) * 60);
        if (num < 0)
            return "-" + du + "°" + fen + "′" + miao + "″";
        return du + "°" + fen + "′" + miao + "″";
    }

    public static String _10To60_len2(String latlng) {
        double num = Double.parseDouble(latlng);
        int du = (int) Math.floor(Math.abs(num));    //获取整数部分
        double temp = getdPoint(Math.abs(num)) * 60;
        int fen = (int) Math.floor(temp); //获取整数部分
        double miao = getdPoint(temp * 60);
        String s = DecimalFormats.getFormat("00.00").format(miao);
        if (num < 0)
            return "-" + du + "°" + fen + "′" + s + "″";
        return du + "°" + fen + "′" + s + "″";
    }

    /**
     * 60进制转10进制
     *
     * @param latlng
     * @return
     */
    public static double _60To10(String latlng) {
        double du = Double.parseDouble(latlng.substring(0, latlng.indexOf("°")));
        double fen = Double.parseDouble(latlng.substring(latlng.indexOf("°") + 1, latlng.indexOf("′")));
        double miao = Double.parseDouble(latlng.substring(latlng.indexOf("′") + 1, latlng.indexOf("″")));
        if (du < 0)
            return -(Math.abs(du) + (fen + (miao / 60)) / 60);
        return du + (fen + (miao / 60)) / 60;
    }

    //获取小数部分
    private static double getdPoint(double num) {
        double d = num;
        int fInt = (int) d;
        BigDecimal b1 = new BigDecimal(Double.toString(d));
        BigDecimal b2 = new BigDecimal(Integer.toString(fInt));
        double dPoint = b1.subtract(b2).floatValue();
        return dPoint;
    }
}
