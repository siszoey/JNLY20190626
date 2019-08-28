package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SelectContainer extends BaseContainer {
    /**
     * 选择集
     */
    private final Map<LayerNode, List<Feature>> selResult = new LinkedHashMap();

    FeatureLayer.SelectionMode mode;

    public SelectContainer setting(FeatureLayer.SelectionMode mode) {
        this.mode = mode;
        return this;
    }

    public Map<LayerNode, List<Feature>> getSelResult() {
        return selResult;
    }

    /**
     * 获取选择集中 有数据的数量
     *
     * @return
     */
    public int hasDataCount() {
        int count = 0;
        List<Feature> features;
        for (LayerNode node : selResult.keySet()) {
            features = selResult.get(node);
            if (features != null && features.size() > 0) count++;
        }
        return count;
    }

    public Map<LayerNode, List<Feature>> getHasDataResult() {
        if (hasDataCount() == 0) return null;
        List<Feature> features;
        Map<LayerNode, List<Feature>> res = new LinkedHashMap();
        for (LayerNode node : selResult.keySet()) {
            features = selResult.get(node);
            if (features != null && features.size() > 0) {
                res.put(node, features);
            }
        }
        return res;
    }


    public void clearSelection(final LayerNode node) {
        selResult.remove(node);
        if (node == null) return;
        LayerContent layerContent = node.getLayerContent();
        if (layerContent instanceof FeatureLayer) {
            FeatureLayer featureLayer = (FeatureLayer) layerContent;
            featureLayer.clearSelection();
        }
    }

    public void clearSelection(final List<LayerNode> nodes) {
        if (nodes == null) return;
        for (LayerNode node : nodes) {
            clearSelection(node);
        }
    }

    public void queryByGeometry(final List<LayerNode> nodes, Geometry geometry) {
        selResult.clear();
        if (nodes == null) return;
        for (LayerNode node : nodes) {
            queryByGeometry(node, geometry, null);
        }
    }

    public void queryByGeometry(final List<LayerNode> nodes, Geometry geometry, final ICallBack iCallBack) {
        selResult.clear();
        try {
            if (nodes == null) return;
            for (LayerNode node : nodes) {
                queryByGeometry(node, geometry, iCallBack);
            }
        } catch (Exception e) {
            if (iCallBack != null) iCallBack.fail(e);
        }
    }

    public void queryByGeometry(final LayerNode node, Geometry geometry, final ICallBack iCallBack) {
        try {
            if (node == null) return;
            queryFeaByGeometry(node, geometry, iCallBack);
        } catch (Exception e) {
            if (iCallBack != null) iCallBack.fail(e);
        }
    }

    private void queryFeaByGeometry(LayerNode node, Geometry geometry, ICallBack iCallBack) {
        LayerContent layerContent = node.getLayerContent();
        if (layerContent instanceof FeatureLayer) {
            queryFeaLayerByGeometry(node, (FeatureLayer) layerContent, geometry, iCallBack);
        }
        if (layerContent instanceof ArcGISMapImageSublayer) {
            queryFeaLayerByGeometry(node, (ArcGISMapImageSublayer) layerContent, geometry, iCallBack);
        }
    }

    private void queryFeaLayerByGeometry(LayerNode node, FeatureLayer layer, Geometry geometry, ICallBack iCallBack) {
        Map res = new HashMap();
        QueryParameters params = new QueryParameters();
        params.setGeometry(geometry);
        params.setReturnGeometry(true);
        if (mode == null) mode = FeatureLayer.SelectionMode.NEW;
        final ListenableFuture<FeatureQueryResult> future = layer.selectFeaturesAsync(params, mode);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = future.get();
                    Iterator<Feature> iterator = result.iterator();
                    Feature feature;
                    List<Feature> features = new ArrayList<>();
                    while (iterator.hasNext()) {
                        feature = iterator.next();
                        features.add(feature);
                    }
                    res.put(node, features);
                    selResult.put(node, features);
                    if (iCallBack != null) iCallBack.success(res);
                } catch (Exception e) {
                    if (iCallBack != null) iCallBack.fail(e);
                }
            }
        });
    }


    private void queryFeaLayerByGeometry(LayerNode node, ArcGISMapImageSublayer layer, Geometry geometry, ICallBack iCallBack) {
        ServiceFeatureTable featureTable = layer.getTable();
        if (featureTable == null) featureTable = new ServiceFeatureTable(node.getUri());
        Map res = new HashMap();
        QueryParameters params = new QueryParameters();
        params.setGeometry(geometry);
        params.setReturnGeometry(true);
        final ListenableFuture<FeatureQueryResult> future = featureTable.queryFeaturesAsync(params);
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult result = future.get();
                    Iterator<Feature> iterator = result.iterator();
                    Feature feature;
                    List<Feature> features = new ArrayList<>();
                    while (iterator.hasNext()) {
                        feature = iterator.next();
                        features.add(feature);
                    }
                    res.put(node, features);
                    selResult.put(node, features);
                    if (iCallBack != null) iCallBack.success(res);
                } catch (Exception e) {
                    if (iCallBack != null) iCallBack.fail(e);
                }
            }
        });
    }


    public interface ICallBack {
        public void success(Map<LayerNode, List<Feature>> res);

        public void fail(Exception e);
    }
}