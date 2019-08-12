package com.titan.jnly.vector.tool;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.utils.ToastUtil;
import com.titan.jnly.common.uitls.Constant;
import com.titan.jnly.common.uitls.ConverterUtils;
import com.titan.jnly.vector.bean.MyLayer;
import com.titan.jnly.vector.inter.IMap;
import com.titan.jnly.vector.bean.RepealInfo;
import com.titan.jnly.vector.inter.ValueBack;
import com.titan.jnly.vector.util.DatabaseHelper;
import com.titan.jnly.vector.util.GisUtils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SketchEditorTools {

    private MapView mapView;
    private Context context;
    private ArcMap arcMap;

    public SketchEditorTools(ArcMap arcmap) {
        this.arcMap = arcmap;
        this.mapView = arcmap.getMapView();
        this.context = arcmap.getContext();
    }

    /**
     * 添加小班  手动画线添加小班 检查是否有小班号或者地块编号字段
     */
    public void addLineToLayer(final FeatureTable ftable, final Polyline line, Map<String, Object> map) {

        if (map == null) {
            GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) ftable;
            Feature feature = table.createFeature(table.getFeatureTemplates().get(0));
            map = feature.getAttributes();
        }
        final Map<String, Object> oMap = map;

        addPolygon(ftable, line, oMap, "");
    }

    /**
     * 线转面 添加面 进行拓扑相交检查
     */
    private void addPolygon(final FeatureTable table, final Polyline line, Map<String, Object> map, String xbh) {
        try {

            final Geometry geo = GisUtils.LineToPolygon(line, mapView);
            Geometry geometry = GeometryEngine.project(geo, table.getSpatialReference());
            if (map == null) {
                GeodatabaseFeatureTable tb = (GeodatabaseFeatureTable) table;
                Feature feature = tb.createFeature(tb.getFeatureTemplates().get(0));
                map = feature.getAttributes();
            }
            final Map<String, Object> oMap = map;
            final Feature feature = Objects.requireNonNull(table).createFeature(map, geometry);
            //拓扑相交检查
            DatabaseHelper.checkFeature(table, feature, new ValueBack() {
                @Override
                public void onSuccess(Object o) {
                    FeatureQueryResult queryResult = (FeatureQueryResult) o;
                    Iterator<Feature> iterator = queryResult.iterator();
                    ArrayList<Feature> features = new ArrayList<>();
                    while (iterator.hasNext()) {
                        features.add(iterator.next());
                    }
                    if (features.size() > 0) {
                        //有拓扑相交时
                        getnewFeature(table, oMap, feature.getGeometry(), features, xbh);
                    } else {
                        addFeatureToLayer(table, feature, xbh);
                    }
                }

                @Override
                public void onFail(@NonNull String code) {

                }

                @Override
                public void onGeometry(@NonNull Geometry geometry) {

                }
            });

            Envelope envelope = table.getExtent();
            Geometry fEnv = GeometryEngine.simplify(envelope);
            boolean flag = GeometryEngine.contains(fEnv, geometry);


        } catch (Exception e) {
            ToastUtil.showLong(context, "添加小班错误：" + e);
            Log.e("tag", "添加小班错误：" + e);
        }
    }


    public void addFeature(final FeatureTable feaTable, Geometry geo, Map<String, Object> map, ICallBack iCallBack) {
        try {
            Geometry geometry = GeometryEngine.project(geo, feaTable.getSpatialReference());
            final GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) feaTable;
            if (map == null) {
                Feature fe = table.createFeature(table.getFeatureTemplates().get(0));
                map = fe.getAttributes();
            }
            final Feature feature = Objects.requireNonNull(feaTable).createFeature(map, geometry);
            final ListenableFuture<Void> result = feaTable.addFeatureAsync(feature);
            result.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        result.get();
                        if (result.isDone()) {
                            if (iCallBack != null) iCallBack.ok();
                        }
                    } catch (Exception e) {
                        ToastUtil.showLong(context, "要素添加错误：" + e);
                    }
                }
            });

        } catch (Exception e) {
            ToastUtil.showLong(context, "要素添加错误：" + e);
            Log.e("tag", "要素添加错误：" + e);
        }
    }


    /**
     * 面添加  原始数据为面时
     */
    public void addGeometry(final FeatureTable featureTable, final Geometry geol, Map<String, Object> map, String xbh) {
        try {

            Geometry geometry = GeometryEngine.project(geol, featureTable.getSpatialReference());
            final GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) featureTable;

            if (map == null) {
                Feature fe = table.createFeature(table.getFeatureTemplates().get(0));
                map = fe.getAttributes();
            }
            final Map<String, Object> oMap = map;
            final Feature feature = Objects.requireNonNull(featureTable).createFeature(map, geometry);
            //拓扑相交检查
            DatabaseHelper.checkFeature(table, feature, new ValueBack() {
                @Override
                public void onGeometry(Geometry geometry) {

                }

                @Override
                public void onSuccess(Object o) {
                    FeatureQueryResult queryResult = (FeatureQueryResult) o;
                    Iterator<Feature> iterator = queryResult.iterator();
                    ArrayList<Feature> features = new ArrayList<>();

                    while (iterator.hasNext()) {
                        features.add(iterator.next());
                    }

                    if (features.size() > 0) {
                        getnewFeature(table, oMap, geometry, features, xbh);
                    } else {

                        addFeatureToLayer(table, feature, xbh);
                    }
                }

                @Override
                public void onFail(String info) {

                }
            });

            Envelope envelope = featureTable.getExtent();
            Geometry fEnv = GeometryEngine.simplify(envelope);
            boolean flag = GeometryEngine.contains(fEnv, geometry);
            /*if (!flag) {
                ToastUtil.showLong(mapView.getContext(), "勾绘小班范围超出图层边界范围");
                return;
            }*/
        } catch (Exception e) {
            ToastUtil.showLong(context, "添加小班错误：" + e);
            Log.e("tag", "添加小班错误：" + e);
        }
    }


    /**
     * 添加小班数据
     */
    private void addFeatureToLayer(final FeatureTable table, final Feature feature, String xbh) {
        Map<String, Object> map = feature.getAttributes();

        if (map.containsKey("MIANJI")) {
            double value = Math.abs(GeometryEngine.area((Polygon) feature.getGeometry()) * 0.0001);
            String area = Constant.disFormat.format(value);
            map.put("MIANJI", ConverterUtils.toDouble(area));
        }

        if (map.containsKey("UID")) {
            String uid = UUID.randomUUID().toString();
            map.put("UID", uid);
        }

        //自动添加DKBH 出现sql错误
        if (map.containsKey("DKBH")) {
            map.put("DKBH", xbh);
        }

        final ListenableFuture<Void> result = table.addFeatureAsync(feature);
        result.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    result.get();
                    if (result.isDone()) {
                        //添加成功
                        Log.e("tag", "小班添加成功");
                        if (mapView.getSketchEditor() != null) {
                            mapView.getSketchEditor().clearGeometry();
                        }
                        addRepealInfo(feature, table, "add", null);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    ToastUtil.showLong(mapView.getContext(), e.getMessage());
                    Log.e("tag", "小班添加失败：" + e);
                }
            }
        });
    }

    private void getnewFeature(final FeatureTable table, Map<String, Object> map, Geometry geometry, ArrayList<Feature> features, String xbbh) {

        Polyline line;

        if (geometry.getGeometryType() == GeometryType.POLYLINE) {
            line = (Polyline) geometry;
        } else if (geometry.getGeometryType() == GeometryType.POLYGON) {
            line = ((Polygon) geometry).toPolyline();
        } else {
            ToastUtil.showLong(mapView.getContext(), "数据类型错误");
            return;
        }

        ArrayList<Polyline> newBoundaries = new ArrayList<>();
        newBoundaries.add(line);

        /*GraphicsOverlay overlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(overlay);
        Graphic graphic = new Graphic(line, new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 20));
        overlay.getGraphics().add(graphic);*/

        ArrayList<Polygon> existingBoundaries = new ArrayList<>();
        for (Feature feature1 : features) {
            existingBoundaries.add((Polygon) feature1.getGeometry());

            /*overlay.getGraphics().clear();
            Graphic graphic1 = new Graphic(feature1.getGeometry(), new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.RED, null));
            overlay.getGraphics().add(graphic1);*/

        }

        List<Polygon> list = GeometryEngine.autoComplete(existingBoundaries, newBoundaries);

        if (list.size() == 0) {
            return;
        }

        Feature feature = table.createFeature(map, list.get(0));

        addFeatureToLayer(table, feature, xbbh);
    }

    /**
     * 共边增班
     */
    public void addFeatureGb(final FeatureTable table, final Geometry drawline, final ArrayList<Feature> features) {
        Feature feature = features.get(0);
        Map<String, Object> map = feature.getAttributes();
        addGbs(table, drawline, features, "");
    }

    private void addGbs(final FeatureTable featureTable, Geometry drawline, ArrayList<Feature> features, String xbbh) {
        Polyline polyline = (Polyline) GeometryEngine.project(drawline, featureTable.getSpatialReference());

        ArrayList<Polyline> newBoundaries = new ArrayList<>();
        newBoundaries.add(polyline);


        ArrayList<Polygon> existingBoundaries = new ArrayList<>();
        for (Feature feature : features) {
            existingBoundaries.add((Polygon) feature.getGeometry());
        }

        List<Polygon> list = GeometryEngine.autoComplete(existingBoundaries, newBoundaries);

        if (list.size() == 0) {
            return;
        }

        GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) featureTable;
        Feature feature1 = table.createFeature(table.getFeatureTemplates().get(0));
        addFeatureToLayer(table, feature1, xbbh);

    }


    private void addGb(final MyLayer myLayer, Geometry drawline, ArrayList<Feature> features, String xbh) {
        Polyline polyline = (Polyline) GeometryEngine.project(drawline, myLayer.getTable().getSpatialReference());

        if (polyline.getParts().size() == 0) {
            return;
        }
        Point startPoint = polyline.getParts().get(0).getStartPoint();
        Point endPoint = polyline.getParts().get(0).getEndPoint();

        Polygon polygon = (Polygon) features.get(0).getGeometry();
        boolean startflag = GeometryEngine.within(startPoint, polygon);
        boolean endflag = GeometryEngine.within(endPoint, polygon);
        if (startflag && endflag) {
            ArrayList<Polyline> newBoundaries = new ArrayList<>();
            newBoundaries.add(polyline);

            ArrayList<Polygon> existingBoundaries = new ArrayList<>();
            existingBoundaries.add(polygon);

            List<Polygon> list = GeometryEngine.autoComplete(existingBoundaries, newBoundaries);
            GeodatabaseFeatureTable table = (GeodatabaseFeatureTable) myLayer.getTable();
            Feature feature1 = table.createFeature(table.getFeatureTemplates().get(0));
            Map<String, Object> map = feature1.getAttributes();
            map.put("XBH", xbh);

            final Feature feature = myLayer.getLayer().getFeatureTable().createFeature(map, list.get(0));
            final ListenableFuture<Void> result = myLayer.getTable().addFeatureAsync(feature);
            result.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        result.get();
                        if (result.isDone()) {
                            //添加成功
                            Log.e("tag", "小班添加成功");
                            mapView.getSketchEditor().clearGeometry();
                            String id = String.valueOf(feature.getAttributes().get("objectid"));
                            Log.e("tag", "id:" + id);
                            addRepealInfo(feature, myLayer.getTable(), "add", null);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        Log.e("tag", "小班添加失败：" + e);
                    }
                }
            });
        } else {
            ToastUtil.showLong(mapView.getContext(), "拓扑错误");
        }
    }


    /**
     * 删除小班
     */
    public void delFeature(final FeatureTable feaTable, final Feature feature) {

        DatabaseHelper.getFeature(feaTable, feature, new ValueBack() {
            @Override
            public void onSuccess(Object o) {
                String value = o.toString();
                if (value.equals("1")) {
                    delete(feaTable, feature);
                } else {
                    ToastUtil.showLong(context, "小班不存在");
                }
            }

            @Override
            public void onFail(@NonNull String code) {

            }

            @Override
            public void onGeometry(@NonNull Geometry geometry) {

            }
        });
    }

    /**
     * 删除小班
     */
    public void delFeature(final MyLayer myLayer, final Feature feature, final List<Feature> features) {

        DatabaseHelper.getFeature(myLayer, feature, new ValueBack() {
            @Override
            public void onSuccess(Object o) {
                String value = o.toString();
                if (value.equals("1")) {
                    delete(myLayer, feature, features);
                } else {
                    ToastUtil.showLong(context, "小班不存在");
                }
            }

            @Override
            public void onFail(@NonNull String code) {

            }

            @Override
            public void onGeometry(@NonNull Geometry geometry) {

            }
        });
    }

    private void delete(final FeatureTable feaTable, final Feature feature) {
        try {
            final ListenableFuture<Void> future = feaTable.deleteFeatureAsync(feature);
            future.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        future.get();
                        if (future.isDone()) {
                            Log.e("tag", "小班删除成功");
                            addRepealInfo(null, feaTable, "del", feature);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("tag", "删除小班失败：" + e);
        }
    }

    private void delete(final MyLayer myLayer, final Feature feature, final List<Feature> features) {
        try {
            final ListenableFuture<Void> future = myLayer.getTable().deleteFeatureAsync(feature);
            future.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        future.get();
                        if (future.isDone()) {
                            Log.e("tag", "小班删除成功");
                            features.remove(feature);
                            addRepealInfo(null, myLayer.getTable(), "del", feature);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("tag", "删除小班失败：" + e);
        }
    }

    /**
     * features 选择的多个小班数据
     * feature  保留属性的小班
     * 小班合并
     */
    public void hebing(final MyLayer myLayer, final List<Feature> features, final Feature feature) {
        List<Geometry> list = new ArrayList<>();
        final Geometry bGeometry = feature.getGeometry();
        for (Feature f : features) {
            list.add(f.getGeometry());
        }
        Log.e("tag", "id1:" + String.valueOf(feature.getAttributes().get("objectid")));
        final Geometry unionGeometry = GeometryEngine.union(list);
        feature.setGeometry(unionGeometry);
        final ListenableFuture<Void> future = myLayer.getTable().updateFeatureAsync(feature);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    future.get();
                    if (future.isDone()) {
                        Log.e("tag", "小班合并成功");
                        Log.e("tag", "id2:" + String.valueOf(feature.getAttributes().get("objectid")));

                        //addRepealInfo(feature, features, myLayer.getTable(), "hebing");
                        Feature bfeature = myLayer.getTable().createFeature(feature.getAttributes(), bGeometry);
                        addRepealInfo(feature, myLayer.getTable(), "update", bfeature);

                        String id = String.valueOf(feature.getAttributes().get("objectid"));

                        Iterator iterator = features.iterator();
                        while (iterator.hasNext()) {
                            Feature feature1 = (Feature) iterator.next();
                            String vlaue = feature1.getAttributes().get("objectid").toString();
                            if (!id.equals(vlaue)) {
                                delFeature(myLayer, feature1, features);
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Log.e("tag", "小班合并失败：" + e);
                }
            }
        });
    }

    /**
     * 小班切割
     */
    public void cutFeature(final FeatureTable table, Geometry drawGeometry, Feature feature) {
        Geometry bGeometry = feature.getGeometry();
        Geometry geometry2 = GeometryEngine.project(drawGeometry, feature.getFeatureTable().getSpatialReference());
        boolean flag = GeometryEngine.intersects(feature.getGeometry(), geometry2);
        if (!flag) {
            ToastUtil.showLong(context, "拓扑错误");
            return;
        }
        Polyline polyline = (Polyline) GeometryEngine.project(drawGeometry, feature.getFeatureTable().getSpatialReference());
        List<Geometry> list = GeometryEngine.cut(feature.getGeometry(), polyline);
        Geometry maxGeometry = null;
        if (list.size() > 1) {
            maxGeometry = list.get(0);
        }
        for (Geometry g : list) {
            if (maxGeometry != null && !maxGeometry.isEmpty() &&
                    GeometryEngine.area((Polygon) g) > GeometryEngine.area((Polygon) maxGeometry)) {
                maxGeometry = g;
            }
        }

        ArrayList<Geometry> tempList = new ArrayList<>(list);
        tempList.remove(maxGeometry);

        if (maxGeometry != null && !maxGeometry.isEmpty()) {
            //更新原有小班
            feature.setGeometry(maxGeometry);

            Feature bfeature = table.createFeature(feature.getAttributes(), bGeometry);

            updataFeature(table, feature, bfeature);

            Map<String, Object> map = feature.getAttributes();
            if (map.containsKey("MIANJI")) {
                double value = Math.abs(GeometryEngine.area((Polygon) feature.getGeometry()) * 0.0001);
                String area = Constant.disFormat.format(value);
                map.put("MIANJI", ConverterUtils.toDouble(area));
            }

            Feature ufeature = table.createFeature(map, feature.getGeometry());
            updataFeature(table, ufeature, bfeature);
        }


        for (final Geometry g : tempList) {
            addGeometry(table, (Polygon) g, feature.getAttributes(), "");


        }
    }

    /**
     * 修班保存
     */
    public void editor(FeatureTable table, Polyline drawline, ArrayList<Feature> features) {

        //修班分两种情况
        // 1、裁剪部分边界 保留面积比较大的部分
        // 2、增加部分边界
        try {
            if (features.size() == 1) {

                final Feature feature = features.get(0);

                Polyline polyline = (Polyline) GeometryEngine.project(drawline, table.getSpatialReference());
                if (polyline.getParts().size() == 0) {
                    return;
                }
                Point startPoint = polyline.getParts().get(0).getStartPoint();
                Point endPoint = polyline.getParts().get(0).getEndPoint();

                Polygon polygon = (Polygon) feature.getGeometry();
                boolean startflag = GeometryEngine.within(startPoint, polygon);
                boolean endflag = GeometryEngine.within(endPoint, polygon);
                if (startflag && endflag) {
                    ArrayList<Polyline> newBoundaries = new ArrayList<>();
                    newBoundaries.add(polyline);

                    ArrayList<Polygon> existingBoundaries = new ArrayList<>();
                    existingBoundaries.add(polygon);

                    List<Polygon> list = GeometryEngine.autoComplete(existingBoundaries, newBoundaries);
                    ArrayList<Geometry> geometries = new ArrayList<>();
                    geometries.add(list.get(0));
                    geometries.add(polygon);

                    Geometry union = GeometryEngine.union(geometries);
                    feature.setGeometry(union);

                    Feature bfeature = table.createFeature(feature.getAttributes(), polygon);
                    upDateFeature(table, feature, bfeature);

                } else if (!startflag && !endflag) {

                    List<Geometry> list = GeometryEngine.cut(polygon, polyline);
                    Geometry geometry1 = null;
                    if (list.size() > 1) {
                        geometry1 = list.get(0);
                    }
                    for (Geometry g : list) {
                        if (geometry1 != null && !geometry1.isEmpty() &&
                                GeometryEngine.area((Polygon) g) > GeometryEngine.area((Polygon) geometry1)) {
                            geometry1 = g;
                        }
                    }
                    if (geometry1 != null && !geometry1.isEmpty()) {
                        feature.setGeometry(geometry1);

                        Feature bfeature = table.createFeature(feature.getAttributes(), polygon);
                        updataFeature(table, feature, bfeature);

                        Map<String, Object> map = feature.getAttributes();
                        if (map.containsKey("MIANJI")) {
                            double value = Math.abs(GeometryEngine.area((Polygon) feature.getGeometry()) * 0.0001);
                            String area = Constant.disFormat.format(value);
                            map.put("MIANJI", ConverterUtils.toDouble(area));
                        }

                        Feature nfeature = table.createFeature(map, feature.getGeometry());
                        updataFeature(table, nfeature, bfeature);
                    }
                }
            }
        } catch (Exception e) {
            ToastUtil.showLong(context, "修班异常：" + e);
            Log.e("tag", "修班异常：" + e);
        }
    }

    /**
     * feature 修改后的feature
     * bfeature 修改前的 图形
     */
    private void upDateFeature(final FeatureTable table, final Feature feature, final Feature bfeature) {

        DatabaseHelper.checkFeature(table, feature, new ValueBack() {
            @Override
            public void onSuccess(Object o) {
                FeatureQueryResult queryResult = (FeatureQueryResult) o;
                Iterator<Feature> iterator = queryResult.iterator();
                ArrayList<Feature> features = new ArrayList<>();

                while (iterator.hasNext()) {
                    features.add(iterator.next());
                }

                if (features.size() > 1) {

                    Polyline polyline = ((Polygon) feature.getGeometry()).toPolyline();

                    ArrayList<Polyline> newBoundaries = new ArrayList<>();
                    newBoundaries.add(polyline);

                    ArrayList<Polygon> existingBoundaries = new ArrayList<>();
                    for (Feature fe : features) {
                        existingBoundaries.add((Polygon) fe.getGeometry());
                    }

                    List<Polygon> list = GeometryEngine.autoComplete(existingBoundaries, newBoundaries);

                    for (Polygon polygon : list) {
                        ArrayList<Geometry> geometries = new ArrayList<>();
                        geometries.add(polygon);
                        geometries.add(bfeature.getGeometry());

                        Geometry union = GeometryEngine.union(geometries);
                        feature.setGeometry(union);

                        updataFeature(table, feature, bfeature);

                        Map<String, Object> map = feature.getAttributes();
                        if (map.containsKey("MIANJI")) {
                            double value = Math.abs(GeometryEngine.area((Polygon) feature.getGeometry()) * 0.0001);
                            String area = Constant.disFormat.format(value);
                            map.put("MIANJI", ConverterUtils.toDouble(area));
                        }

                        Feature nfeature = table.createFeature(map, feature.getGeometry());
                        updataFeature(table, nfeature, bfeature);
                    }
                    //Feature nFeature = myLayer.getTable().createFeature(feature.getAttributes(),union);
                } else if (features.size() == 1) {
                    updataFeature(table, feature, bfeature);

                    Map<String, Object> map = feature.getAttributes();
                    if (map.containsKey("MIANJI")) {
                        double value = Math.abs(GeometryEngine.area((Polygon) feature.getGeometry()) * 0.0001);
                        String area = Constant.disFormat.format(value);
                        map.put("MIANJI", ConverterUtils.toDouble(area));
                    }

                    Feature nfeature = table.createFeature(map, feature.getGeometry());
                    updataFeature(table, nfeature, bfeature);
                }
            }

            @Override
            public void onFail(@NonNull String code) {

            }

            @Override
            public void onGeometry(@NonNull Geometry geometry) {

            }
        });
    }

    private void updataFeature(final FeatureTable table, Feature feature, final Feature bfeature) {
        final ListenableFuture<Void> future = Objects.requireNonNull(table).updateFeatureAsync(feature);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    future.get();
                    if (future.isDone()) {
                        //添加成功
                        Log.e("tag", "小班修改成功");
                        if (mapView.getSketchEditor() != null) {
                            mapView.getSketchEditor().clearGeometry();
                        }
                        addRepealInfo(feature, table, "update", bfeature);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Log.e("tag", "小班修改失败：" + e);
                }
            }
        });
    }

    /**
     * 小班选择
     */
    public void queryFeature(MyLayer myLayer, Geometry geo, final List<Feature> features, final ISketchEditor editor) {

        Geometry geometry = GeometryEngine.project(geo, myLayer.getLayer().getSpatialReference());

        QueryParameters parameters = new QueryParameters();
        parameters.setGeometry(geometry);
        parameters.setReturnGeometry(true);
        parameters.setSpatialRelationship(QueryParameters.SpatialRelationship.INTERSECTS);
        FeatureLayer layer = myLayer.getLayer();
        final ListenableFuture<FeatureQueryResult> listenableFuture = layer.selectFeaturesAsync(parameters, FeatureLayer.SelectionMode.NEW);
        listenableFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = listenableFuture.get();
                    Iterator<Feature> it = result.iterator();
                    Feature queryFeature = null;
                    while (it.hasNext()) {
                        queryFeature = it.next();
                        if (!features.contains(queryFeature)) {
                            features.add(queryFeature);
                            if (mapView.getSketchEditor() != null) {
                                mapView.getSketchEditor().clearGeometry();
                            }
                        }
                    }

                    if (queryFeature != null) {
                        editor.getSelLayers().add(myLayer);
                    }

                    editor.queryLayer(features);

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    ToastUtil.showLong(context, "小班选择失败：" + e);
                    Log.e("tag", "小班选择失败：" + e);
                }
            }
        });
    }

    /**
     * 撤销
     */
    public void repealEdit(IMap iMap) {
        ArrayList<RepealInfo> list = iMap.getRepairList();
        if (list.isEmpty()) {
            ToastUtil.showLong(context, "无法回退");
            return;
        }
        final RepealInfo repealInfo = list.get(list.size() - 1);
        if (repealInfo.getType().equals("add")) {
            Feature feature = repealInfo.getUgeometry();
            final ListenableFuture<Void> future = repealInfo.getTable().deleteFeatureAsync(feature);
            future.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        future.get();
                        if (future.isDone()) {
                            iMap.getRepairList().remove(repealInfo);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        Log.e("tag", "add回退失败：" + e);
                    }
                }
            });
        } else if (repealInfo.getType().equals("del")) {
            Feature bfeature = repealInfo.getBfeature();
            final ListenableFuture<Void> future = repealInfo.getTable().addFeatureAsync(bfeature);
            future.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        future.get();
                        if (future.isDone()) {
                            iMap.getRepairList().remove(repealInfo);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        Log.e("tag", "del回退失败：" + e);
                    }
                }
            });
        } else if (repealInfo.getType().equals("update")) {
            Feature ufeature = repealInfo.getUgeometry();
            Feature bfeature = repealInfo.getBfeature();

            ufeature.setGeometry(bfeature.getGeometry());
            final ListenableFuture<Void> future = repealInfo.getTable().updateFeatureAsync(ufeature);
            future.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        future.get();
                        if (future.isDone()) {
                            iMap.getRepairList().remove(repealInfo);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        Log.e("tag", "update回退失败：" + e);
                    }
                }
            });
        }
    }

    public void updateFeatures(FeatureTable table, List<Feature> features) {
        if (table == null || features == null || features.size() == 0) return;
        for (Feature feature : features) {
            updateFeature(table, feature, null);
        }
    }

    public void updateFeature(FeatureTable table, Feature feature, final ICallBack iCallBack) {
        final ListenableFuture future = table.updateFeatureAsync(feature);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    future.get();
                    if (iCallBack != null) iCallBack.ok();
                } catch (Exception e) {
                    ToastUtil.showLong(context, e.getMessage());
                }
            }
        });
    }

    /**
     * 添加小班修改信息
     *
     * @param ufeature 修改后的小班
     * @param table
     * @param type
     * @param bFeature 修改前的小班
     */
    public void addRepealInfo(Feature ufeature, FeatureTable table, String type, Feature bFeature) {
        RepealInfo repealInfo = new RepealInfo();
        repealInfo.setUgeometry(ufeature);
        repealInfo.setTable(table);
        repealInfo.setType(type);
        repealInfo.setBfeature(bFeature);
        //iMap.getRepairList().add(repealInfo);
        mapView.invalidate();
    }

    private void addRepealInfo(Feature feature, List<Feature> features, FeatureTable table, String type) {
        RepealInfo repealInfo = new RepealInfo();
        repealInfo.setUgeometry(feature);
        repealInfo.setFeatures(features);
        repealInfo.setTable(table);
        repealInfo.setType(type);
        //iMap.getRepairList().add(repealInfo);
    }

    public interface ISketchEditor {
        //查询小班
        void queryLayer(List<Feature> featureList);

        ArrayList<MyLayer> getSelLayers();
    }

    private void addFiled() {

    }

    public interface ICallBack {
        public void ok();
    }
}
