package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zy on 2019/5/9.
 */

public class QueryContainer extends BaseContainer {

    public void queryFeatures(final LayerNode node, QueryParameters params, final ICallBack iCallBack) {
        try {
            if (iCallBack != null) iCallBack.ready();
            ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(node.getUri());
            final ListenableFuture<FeatureQueryResult> future = serviceFeatureTable.queryFeaturesAsync(params, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
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
        } catch (Exception e) {
            if (iCallBack != null) iCallBack.fail(e);
        }
    }

    public void queryFeatures(String uri, QueryParameters params, final ICallBack iCallBack) {
        try {
            if (iCallBack != null) iCallBack.ready();
            ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(uri);
            final ListenableFuture<FeatureQueryResult> future = serviceFeatureTable.queryFeaturesAsync(params);
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
        } catch (Exception e) {
            if (iCallBack != null) iCallBack.fail(e);
        }
    }

    public interface ICallBack {
        public void ready();

        public void success(List<Feature> features);

        public void fail(Exception e);
    }
}
