package com.lib.bandaid.arcruntime.layer.utils;

import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.LayerList;


public final class DataSourceRead {

    public static void readGeoDatabase(final LayerList list, String path) {
        final Geodatabase geodatabase = new Geodatabase(path);
        geodatabase.loadAsync();
        geodatabase.addDoneLoadingListener(new Runnable() {

            @Override
            public void run() {
                for (GeodatabaseFeatureTable gdbFeatureTable : geodatabase.getGeodatabaseFeatureTables()) {
                    FeatureLayer featureLayer = new FeatureLayer(gdbFeatureTable);
                    list.add(featureLayer);
                }
            }
        });
    }

    public static void readShape(LayerList list, String path) {
        ShapefileFeatureTable layer = null;
        layer = new ShapefileFeatureTable(path);
        list.add(layer.getFeatureLayer());
    }

}
