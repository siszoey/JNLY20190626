package com.lib.bandaid.arcruntime.layer.project;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.layers.ArcGISSublayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledSublayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.layers.BingMapsLayer;
import com.esri.arcgisruntime.layers.EncLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.esri.arcgisruntime.layers.MobileBasemapLayer;
import com.esri.arcgisruntime.layers.OpenStreetMapLayer;
import com.esri.arcgisruntime.layers.RasterLayer;

import java.util.List;

/**
 * Created by zy on 2019/5/27.
 */

public class Parser {
    LayerNode _layerNode;

    public Parser(Layer layer) {
        this._layerNode = new LayerNode();
        iteration(layer);
    }

    private void iteration(Layer layer) {
        List<LayerContent> list = layer.getSubLayerContents();
        LayerContent content;
        _layerNode.setUri(layer.getDescription());
        _layerNode.setName(layer.getName());
        _layerNode.setId(layer.getId());
        _layerNode.setLayerContent(layer);
        LayerNode layerNode;
        for (int i = 0; i < list.size(); i++) {
            content = list.get(i);
            layerNode = new LayerNode();
            _layerNode.addNode(layerNode);
            layerNode.setId(getContentId(content));
            layerNode.setName(content.getName());
            layerNode.setUri(_layerNode.getUri().concat("/").concat(layerNode.getId() + ""));
            if (content.getSubLayerContents().size() == 0) {
                layerNode.setLayerContent(content);
                layerNode.setValid(true);
                continue;
            } else {
                layerNode.setLayerContent(content);
                layerNode.setValid(false);
                iteration(content, layerNode);
            }
        }
    }

    private void iteration(LayerContent content, LayerNode _layerNode) {
        LayerNode layerNode;
        List<LayerContent> list = content.getSubLayerContents();
        for (int i = 0; i < list.size(); i++) {
            content = list.get(i);
            layerNode = new LayerNode();
            layerNode.setId(getContentId(content));
            layerNode.setName(content.getName());
            layerNode.setUri(this._layerNode.getUri().concat("/").concat(layerNode.getId() + ""));
            _layerNode.addNode(layerNode);
            if (content.getSubLayerContents().size() == 0) {
                layerNode.setLayerContent(content);
                layerNode.setValid(true);
                continue;
            } else {
                layerNode.setValid(false);
                iteration(content, layerNode);
            }
        }
    }

    public LayerNode getLayerNode() {
        return _layerNode;
    }

    /**
     *
     */
    public static Object getContentId(LayerContent content) {
        Object id = null;
        if (content instanceof ArcGISSublayer) {
            id = ((ArcGISSublayer) content).getId();
        } else if (content instanceof ArcGISMapImageLayer) {
            id = ((ArcGISMapImageLayer) content).getId();
        } else if (content instanceof ArcGISMapImageSublayer) {
            id = ((ArcGISMapImageSublayer) content).getId();
        } else if (content instanceof FeatureLayer) {
            id = ((FeatureLayer) content).getId();
        } else if (content instanceof RasterLayer) {
            id = ((RasterLayer) content).getId();
        } else if (content instanceof BingMapsLayer) {
            id = ((BingMapsLayer) content).getId();
        } else if (content instanceof EncLayer) {
            id = ((EncLayer) content).getId();
        } else if (content instanceof ArcGISSceneLayer) {
            id = ((ArcGISSceneLayer) content).getId();
        } else if (content instanceof ArcGISTiledLayer) {
            id = ((ArcGISTiledLayer) content).getId();
        } else if (content instanceof ArcGISTiledSublayer) {
            id = ((ArcGISTiledSublayer) content).getId();
        } else if (content instanceof ArcGISVectorTiledLayer) {
            id = ((ArcGISVectorTiledLayer) content).getId();
        } else if (content instanceof MobileBasemapLayer) {
            id = ((MobileBasemapLayer) content).getId();
        } else if (content instanceof OpenStreetMapLayer) {
            id = ((OpenStreetMapLayer) content).getId();
        } else if (content instanceof Layer) {
            id = ((Layer) content).getId();
        }
        return id;
    }
}
