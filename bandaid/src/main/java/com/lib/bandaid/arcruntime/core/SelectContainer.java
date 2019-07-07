package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectContainer extends BaseContainer {

    public void clearSelection(final LayerNode node) {
        if (node == null) return;
        LayerContent layerContent = node.getLayerContent();
        if (layerContent instanceof FeatureLayer) {
            FeatureLayer featureLayer = (FeatureLayer) layerContent;
            featureLayer.clearSelection();
        }
    }

    public void queryByGeometry(final LayerNode node, Geometry geometry, final QueryContainer.ICallBack iCallBack) {
        try {
            if (node == null) return;
            if (iCallBack != null) iCallBack.ready();
            LayerContent layerContent = node.getLayerContent();
            if (layerContent instanceof FeatureLayer) {
                FeatureLayer featureLayer = (FeatureLayer) layerContent;
                queryFeaLayerByGeometry(featureLayer, geometry, iCallBack);
            }
        } catch (Exception e) {
            if (iCallBack != null) iCallBack.fail(e);
        }
    }

    private void queryFeaLayerByGeometry(FeatureLayer featureLayer, Geometry geometry, QueryContainer.ICallBack iCallBack) {
        QueryParameters params = new QueryParameters();
        params.setGeometry(geometry);
        params.setReturnGeometry(true);
        final ListenableFuture<FeatureQueryResult> future = featureLayer.selectFeaturesAsync(params, FeatureLayer.SelectionMode.NEW);
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
                    if (iCallBack != null) iCallBack.success(features);
                } catch (Exception e) {
                    if (iCallBack != null) iCallBack.fail(e);
                }
            }
        });
    }
}