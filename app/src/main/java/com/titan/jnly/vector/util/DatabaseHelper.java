package com.titan.jnly.vector.util;

import android.util.Log;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.titan.jnly.vector.bean.MyLayer;
import com.titan.jnly.vector.inter.ValueBack;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class DatabaseHelper {

    public static void getMaxXbh(MyLayer myLayer, String tbname, final ValueBack callBack) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setWhereClause("XBH = (select MAX(XBH) from " + tbname.trim() + ")");
        final ListenableFuture<FeatureQueryResult> result = myLayer.getTable().queryFeaturesAsync(queryParameters);
        result.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult queryResult = result.get();
                    Iterator iterator = queryResult.iterator();
                    Object xbh = "00000";
                    while (iterator.hasNext()) {
                        Feature queryFeature = (Feature) iterator.next();
                        Map<String, Object> map = queryFeature.getAttributes();
                        xbh = map.get("XBH");
                        //Log.e("MAX",xbh.toString());
                    }
                    callBack.onSuccess(xbh);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void getMaxDkbh(MyLayer myLayer, String tbname, final ValueBack callBack) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setWhereClause("DKBH = (select MAX(DKBH) from " + tbname.trim() + ")");
        final ListenableFuture<FeatureQueryResult> result = myLayer.getTable().queryFeaturesAsync(queryParameters);
        result.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult queryResult = result.get();
                    Iterator iterator = queryResult.iterator();
                    Object xbh = "00";
                    while (iterator.hasNext()) {
                        Feature queryFeature = (Feature) iterator.next();
                        Map<String, Object> map = queryFeature.getAttributes();
                        xbh = map.get("DKBH");
                        //Log.e("MAX",xbh.toString());
                    }
                    callBack.onSuccess(xbh);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    Log.e("00000000000======", e.getMessage());
                }
            }
        });
    }

    public static void getFeature(MyLayer myLayer, Feature feature, final ValueBack callBack) {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setWhereClause("OBJECTID = " + feature.getAttributes().get("OBJECTID"));
        final ListenableFuture<Long> result = myLayer.getTable().queryFeatureCountAsync(queryParameters);
        result.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    long count = result.get();
                    callBack.onSuccess(count);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void checkFeature(MyLayer myLayer, Feature feature, final ValueBack callBack) {
        QueryParameters param = new QueryParameters();
        param.setGeometry(feature.getGeometry());
        //param.setReturnGeometry(true);
        param.setSpatialRelationship(QueryParameters.SpatialRelationship.INTERSECTS);

        final ListenableFuture<FeatureQueryResult> result = myLayer.getTable().queryFeaturesAsync(param);
        result.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    FeatureQueryResult queryResult = result.get();
                    Iterator iterator = queryResult.iterator();
                    int count = 0;
                    while (iterator.hasNext()) {
                        count++;
                        break;
                    }
                    callBack.onSuccess(queryResult);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
