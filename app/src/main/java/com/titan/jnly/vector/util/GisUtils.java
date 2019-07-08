package com.titan.jnly.vector.util;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.mapping.view.MapView;

public class GisUtils {

    /** 判断线段是否自交,形成闭合面 */
    public static Polygon LineToPolygon(Polyline line, MapView mapView) {
        /* 交点 */
        Point point = null;
        int size = line.getParts().get(0).getPointCount();
        int a = 0, b = 0, c = 0, d = 0;
        boolean end = false;
        for (int i = 0; i < size - 2; i++) {
            if (end) {
                break;
            }
            PointCollection points = new PointCollection(mapView.getSpatialReference());
            points.add(line.getParts().get(0).getPoint((i)));
            points.add(line.getParts().get(0).getPoint((i+1)));
            Polyline line1 = new Polyline(points);
            for (int j = i+2; j < size - 1; j++) {
                PointCollection points2 = new PointCollection(mapView.getSpatialReference());
                points2.add(line.getParts().get(0).getPoint(j));
                points2.add(line.getParts().get(0).getPoint(j+1));
                Polyline line2 = new Polyline(points2);
                boolean bl = GeometryEngine.intersects(line2, line1);
                if (bl) {
                    a = i;b = i + 1;
                    c = j;d = j + 1;
                    point = getLineIntersection(line.getParts().get(0).getPoint((a)), line.getParts().get(0).getPoint(b),line.getParts().get(0).getPoint(c), line.getParts().get(0).getPoint(d));
                    if(point != null){
                        end = true;
                        break;
                    }
                }
            }
        }

        PointCollection pointCollection = new PointCollection(mapView.getSpatialReference());
        if (point == null) {

            for (int i = 0; i < size; i++) {
                pointCollection.add(line.getParts().get(0).getPoint(i));
            }
        } else {
            if (!GeometryEngine.isSimple(point)) {
                for (int i = b; i < d; i++) {
                    pointCollection.add(line.getParts().get(0).getPoint(i));
                }
            } else {
                pointCollection.add(point);
                for (int i = b; i < d; i++) {
                    pointCollection.add(line.getParts().get(0).getPoint(i));
                }
            }
        }
        return new Polygon(pointCollection);
    }

    /** 获取两条线段的交点 */
    private static Point getLineIntersection(Point a, Point c, Point b, Point d){
        double e=0,aaa = 0;
        double f = gas(a.getX(), c.getX()) ? 1E10 : (a.getY() - c.getY()) / (a.getX() - c.getX());
        double k = gas(b.getX(), d.getX()) ? 1E10 : (b.getY() - d.getY()) / (b.getX() - d.getX());
        double l = a.getY() - f * a.getX();
        double h = b.getY() - k * b.getX();

        if (gas(f, k)) {
            if (gas(l, h)) {
                double aa,bb,cc,dd;
                boolean f1 = Math.min(a.getX(), c.getX()) < Math.max(b.getX(), d.getX());
                boolean f2 = Math.max(a.getX(), c.getX()) > Math.min(b.getX(), d.getX());
                if (gas(a.getX(), c.getX())){
                    boolean flag = Math.min(a.getY(), c.getY()) < Math.max(b.getY(),d.getY());
                    boolean flag1 = Math.max(a.getY(),c.getY()) > Math.min(b.getY(), d.getY());
                    if (flag || flag1){
                        aa = Math.min(a.getY(), c.getY());
                        bb = Math.min(b.getY(), d.getY());
                        cc = Math.max(a.getY(), c.getY());
                        dd = Math.max(b.getY(), d.getY());
                        aaa = (a.getY() + c.getY() + b.getY() + d.getY() - Math.min(aa, bb) - Math.max(cc, dd)) / 2;
                        e = (aaa - l) / f;
                        return new Point(e, aaa);
                    }else {
                        return null;
                    }
                }else if (f1 || f2){
                    aa = Math.min(a.getX(), c.getX());
                    bb = Math.min(b.getX(), d.getX());
                    cc = Math.max(a.getX(), c.getX());
                    dd = Math.max(b.getX(), d.getX());
                    e = (a.getX() + c.getX() + b.getX() + d.getX() - Math.min(aa, bb) - Math.max(bb,dd)) / 2;
                    aaa = f * e + l;
                    return new Point(e, aaa);
                }else{
                    return null;
                }
            }
            return null;
        }
        boolean f3 = gas(f, 1E10);
        if(f3){
            e = a.getX();
            aaa = k * e + h;
        }else{
            boolean f4 = gas(k, 1E10);
            if(f4){
                e = b.getX();
                aaa = f * e + l;
            }else{
                e = -(l - h) / (f - k);
                if(a.getY() == c.getY()){
                    aaa = a.getY();
                }else{
                    if(b.getY() == d.getY()){
                        aaa = b.getY();
                    }else{
                        aaa = f * e + l;
                    }
                }
            }
        }
        return new Point(e, aaa);
    }

    private static boolean gas(double a,double c){
        return 1E-8 > Math.abs(a - c);
    }


}
