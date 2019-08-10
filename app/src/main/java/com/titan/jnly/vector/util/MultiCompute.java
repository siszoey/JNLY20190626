package com.titan.jnly.vector.util;

import androidx.annotation.NonNull;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Geometry;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.utils.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 古树群和单株直接的关系换算
 */
public final class MultiCompute {

    /**
     * 获取单株调查表里objectid最大的一项
     *
     * @param iCallBack
     * @return
     */
    public static void getLastFeature(@NonNull Feature _feature, @NonNull ICallBack iCallBack) {
        long _objectId = (long) _feature.getAttributes().get("OBJECTID");
        LayerNode layerNode = ArcMap.arcMap.getTocContainer().getLayerNodeByName("古树名木单株调查");
        if (layerNode == null) return;
        FeatureTable single = layerNode.tryGetFeaTable();
        QueryParameters params = new QueryParameters();
        params.getOrderByFields().add(new QueryParameters.OrderBy("OBJECTID", QueryParameters.SortOrder.DESCENDING));
        params.setMaxFeatures(2);
        params.setResultOffset(0);
        ListenableFuture<FeatureQueryResult> future = single.queryFeaturesAsync(params);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = future.get();
                    Iterator<Feature> features = result.iterator();
                    Feature feature = null;
                    long objectId;
                    while (features.hasNext()) {
                        feature = features.next();
                        objectId = (long) _feature.getAttributes().get("OBJECTID");
                        if (_objectId == objectId) continue;
                        break;
                    }
                    if (iCallBack != null)
                        iCallBack.callback(feature);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取单株调查表里objectid最大的一项
     *
     * @param iCallBack
     * @return
     */
    public static void getLastFeature(@NonNull ICallBack iCallBack) {
        LayerNode layerNode = ArcMap.arcMap.getTocContainer().getLayerNodeByName("古树名木单株调查");
        if (layerNode == null) return;
        FeatureTable single = layerNode.tryGetFeaTable();
        QueryParameters params = new QueryParameters();
        params.getOrderByFields().add(new QueryParameters.OrderBy("OBJECTID", QueryParameters.SortOrder.DESCENDING));
        params.setMaxFeatures(1);
        params.setResultOffset(0);
        ListenableFuture<FeatureQueryResult> future = single.queryFeaturesAsync(params);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = future.get();
                    Iterator<Feature> features = result.iterator();
                    Feature feature = null;
                    while (features.hasNext()) {
                        feature = features.next();
                        break;
                    }
                    if (iCallBack != null)
                        iCallBack.callback(feature);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 新增古树群
     *
     * @param
     */
    public static void newCrowd(Geometry geometry, @NonNull IProperty iProperty) {

        LayerNode layerNode = ArcMap.arcMap.getTocContainer().getLayerNodeByName("古树名木单株调查");
        if (layerNode == null) return;
        FeatureTable single = layerNode.tryGetFeaTable();
        QueryParameters params = new QueryParameters();
        params.setGeometry(geometry);
        ListenableFuture<FeatureQueryResult> future = single.queryFeaturesAsync(params);
        List<Feature> list = new ArrayList<>();
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = future.get();
                    Iterator<Feature> features = result.iterator();
                    Feature feature;
                    while (features.hasNext()) {
                        feature = features.next();
                        list.add(feature);
                    }
                    if (iProperty != null)
                        iProperty.computeProperty(list, computeBySingle(list));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Map<String, Object> computeBySingle(List<Feature> list) {
        Map<String, Object> property = new HashMap<>();
        int count = list.size();
        double sumHigh = 0;
        double sumDiam = 0;
        double sumSlope = 0;
        double sumAge = 0;
        Map<String, Object> item;
        for (int i = 0; i < list.size(); i++) {
            item = list.get(i).getAttributes();
            sumHigh += NumberUtil.obj2Number(item.get("SG")).doubleValue();
            sumDiam += NumberUtil.obj2Number(item.get("XJ")).doubleValue();
            sumSlope += NumberUtil.obj2Number(item.get("PODU")).doubleValue();
            sumAge += NumberUtil.obj2Number(item.get("ZSSL")).doubleValue();
        }
        property.put("GSZS", (double) count);
        property.put("LFPJG", count == 0 ? 0 : sumHigh / count);
        property.put("LFPJXW", count == 0 ? 0 : sumDiam / count);
        property.put("PODU", count == 0 ? 0 : sumSlope / count);
        property.put("PJSL", count == 0 ? 0 : sumAge / count);
        return property;
    }


    /**
     * @param single
     * @param multi
     * @param point
     */
    public static void add2Crowd(FeatureTable single, FeatureTable multi, Feature point) {

    }

    public interface IProperty {
        public void computeProperty(List<Feature> features, Map property);
    }

    public interface ICallBack {
        public void callback(Feature feature);
    }
}
