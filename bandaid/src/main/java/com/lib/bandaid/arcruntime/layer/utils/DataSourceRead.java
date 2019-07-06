package com.lib.bandaid.arcruntime.layer.utils;

import com.esri.arcgisruntime.data.Geodatabase;
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.LayerList;

import java.io.File;
import java.util.List;
import java.util.Map;


public final class DataSourceRead {

    public static void readGeoDatabase(final LayerList list, final String path) {
        final Geodatabase geodatabase = new Geodatabase(path);
        geodatabase.loadAsync();
        geodatabase.addDoneLoadingListener(new Runnable() {

            @Override
            public void run() {
                for (GeodatabaseFeatureTable gdbFeatureTable : geodatabase.getGeodatabaseFeatureTables()) {
                    Layer layer = new FeatureLayer(gdbFeatureTable);
                    layer.setDescription(path);
                    list.add(layer);
                }
            }
        });
    }

    public static void readShape(LayerList list, String path) {
        Layer layer = new ShapefileFeatureTable(path).getFeatureLayer();
        layer.setDescription(path);
        list.add(layer);
    }


    public static void read(LayerList list, Map<String, List<File>> tree) {
        List<File> files;
        for (String path : tree.keySet()) {
            files = tree.get(path);
            File file;
            for (int i = 0; i < files.size(); i++) {
                file = files.get(i);
                if (file.getName().toLowerCase().endsWith(".shape")) {
                    readShape(list, file.getPath());
                }
                if (file.getName().toLowerCase().endsWith(".geodatabase")) {
                    readGeoDatabase(list, file.getPath());
                }
            }
        }
    }
}
