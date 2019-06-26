package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.layer.project.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/5/9.
 */

public class TocContainer extends BaseContainer {

    private List<LayerNode> layerNodes = new ArrayList<>();

    private Map<String, Layer> layers = new HashMap<>();

    private List<ILayerLoaded> layersLoaded = new ArrayList<>();

    @Override
    public void ready(List<Layer> layers) {
        if (layers == null) return;
        addLayers(layers);
        for (final Layer layer : layers) {
            layer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    if (layer.getLoadStatus() == LoadStatus.LOADED) {
                        LayerNode node = new Parser(layer).getLayerNode();
                        addLayerNode(node);
                        notifyLayerLoad(node);
                    }
                }
            });
        }
    }

    void addLayer(Layer layer) {
        if (layer == null) return;
        this.layers.put(layer.getId(), layer);
    }

    void addLayers(List<Layer> layers) {
        if (layers == null) return;
        Layer layer;
        for (int i = 0; i < layers.size(); i++) {
            layer = layers.get(i);
            this.layers.put(layer.getId(), layer);
        }
    }

    public void addLayerNode(LayerNode node) {
        layerNodes.add(node);
    }

    public interface ILayerLoaded {
        public void iLayerLoaded(LayerNode node);
    }

    public void addILayerLoaded(ILayerLoaded layerLoad) {
        layersLoaded.add(layerLoad);
    }

    public void notifyLayerLoad(LayerNode node) {
        for (ILayerLoaded layerLoad : layersLoaded) {
            layerLoad.iLayerLoaded(node);
        }
    }

    public List<LayerNode> getLayerNodes() {
        return layerNodes;
    }

    public Layer getLayerByUri(String uri) {
        if (layerNodes == null) return null;
        Layer layer;
        for (LayerNode node : layerNodes) {
            layer = node.filterLayer(uri);
            if (layer != null) return layer;
        }
        return null;
    }
}
