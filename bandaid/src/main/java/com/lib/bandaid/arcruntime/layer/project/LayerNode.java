package com.lib.bandaid.arcruntime.layer.project;

import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geometry.GeometryType;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.esri.arcgisruntime.layers.SublayerList;
import com.lib.bandaid.arcruntime.layer.info.LayerInfo;
import com.lib.bandaid.thread.rx.RxSimpleUtil;
import com.lib.bandaid.utils.HttpSimpleUtil;
import com.lib.bandaid.utils.MapUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/5/24.
 */

public class LayerNode implements Serializable {

    private Object id;

    private String name;

    private String uri;

    private LayerContent layerContent;

    private List<LayerNode> nodes;

    private boolean isValid = false;

    private LayerInfo info;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public LayerContent getLayerContent() {
        return layerContent;
    }

    public void setLayerContent(LayerContent layerContent) {
        this.layerContent = layerContent;
    }

    public FeatureLayer tryGetFeaLayer() {
        if (layerContent == null) return null;
        if (layerContent instanceof FeatureLayer) return (FeatureLayer) layerContent;
        return null;
    }

    public FeatureTable tryGetFeaTable() {
        FeatureLayer featureLayer = tryGetFeaLayer();
        if (featureLayer != null) return featureLayer.getFeatureTable();
        return null;
    }

    public GeometryType tryGetGeometryType() {
        FeatureTable table = tryGetFeaTable();
        if(table!=null)return table.getGeometryType();
        return null;
    }

    public List<LayerNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<LayerNode> nodes) {
        this.nodes = nodes;
    }

    public void addNode(LayerNode node) {
        if (this.nodes == null) this.nodes = new ArrayList<>();
        this.nodes.add(node);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
        if (valid) {
            RxSimpleUtil.simple(new RxSimpleUtil.ISimpleBack<String>() {
                @Override
                public String run() {
                    return HttpSimpleUtil._get(uri + "?f=pjson");
                }

                @Override
                public void success(String s) {
                    info = MapUtil.string2Entity(s, LayerInfo.class);
                }
            });
        }
    }

    public boolean getVisible() {
        //return layerContent == null ? false : layerContent.isVisible();
        return layerContent == null ? true : layerContent.isVisible();
    }

    public LayerInfo getInfo() {
        return info;
    }

    public boolean hasChildNode() {
        return nodes != null && nodes.size() > 0;
    }

    /**
     * 判断子图层是否有不可见的
     *
     * @return
     */
    public boolean hasInVisible() {
        List<LayerNode> temp = getLeafNode();
        if (temp == null) return false;
        for (LayerNode node : temp) {
            if (!node.getVisible()) return true;
        }
        return !getVisible();
    }

    public void setVisible(boolean visible) {
        iteration(this, visible);
    }

    /**
     * 判断是否为本地图层
     *
     * @return
     */
    public boolean isLocal() {
        return !uri.toLowerCase().startsWith("http");
    }

    public List<LayerNode> getLeafNode() {
        List<LayerNode> res = new ArrayList<>();
        if (getLayerContent() != null && !hasChildNode()) {
            //System.out.println(getName());
            res.add(this);
        }
        List<LayerNode> nodes = this.getNodes();
        if (nodes == null) return res;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            getLeafNode(res, _layerNode);
        }
        return res;
    }

    private void getLeafNode(List<LayerNode> in, LayerNode layerNode) {
        if (layerNode.getLayerContent() != null) {
            //System.out.println(layerNode.getName());
            in.add(layerNode);
        }
        List<LayerNode> nodes = layerNode.getNodes();
        if (nodes == null) return;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            getLeafNode(in, _layerNode);
        }
    }

    public Layer filterLayer(String uri) {
        List<LayerNode> nodes = getLeafNode();
        LayerNode node;
        for (int i = 0, len = nodes.size(); i < len; i++) {
            node = nodes.get(i);
            if (node.getUri() != null && node.getUri().equals(uri))
                return (Layer) node.getLayerContent();
        }
        return null;
    }


    //----------------------------------------------------------------------------------------------

    public static void iteration(LayerNode layerNode, boolean visible) {
        if (layerNode == null) return;
        if (layerNode.getLayerContent() != null) {
            try {
                layerNode.getLayerContent().setVisible(visible);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        List<LayerNode> nodes = layerNode.getNodes();
        if (nodes == null) return;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            iteration(_layerNode, visible);
        }
    }
}
