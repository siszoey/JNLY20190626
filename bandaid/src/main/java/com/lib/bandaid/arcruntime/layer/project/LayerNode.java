package com.lib.bandaid.arcruntime.layer.project;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
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
        return layerContent == null ? false : layerContent.isVisible();
    }

    public LayerInfo getInfo() {
        return info;
    }

    /**
     * 判断子图层是否有不可见的
     *
     * @return
     */
    public boolean hasInVisible() {
        List<LayerNode> temp = getLeftNode();
        if (temp == null) return false;
        for (LayerNode node : temp) {
            if (!node.getVisible()) return true;
        }
        return layerContent == null ? false : layerContent.isVisible();
    }

    public void setVisible(boolean visible) {
        iteration(this, visible);
    }

    public List<LayerNode> getLeftNode() {
        List<LayerNode> res = new ArrayList<>();
        if (getLayerContent() != null) {
            System.out.println(getName());
            res.add(this);
        }
        List<LayerNode> nodes = this.getNodes();
        if (nodes == null) return res;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            getLeftNode(res, _layerNode);
        }
        return res;
    }

    private void getLeftNode(List<LayerNode> in, LayerNode layerNode) {
        if (layerNode.getLayerContent() != null) {
            System.out.println(layerNode.getName());
            in.add(layerNode);
        }
        List<LayerNode> nodes = layerNode.getNodes();
        if (nodes == null) return;
        LayerNode _layerNode;
        for (int i = 0; i < nodes.size(); i++) {
            _layerNode = nodes.get(i);
            getLeftNode(in, _layerNode);
        }
    }

    public Layer filterLayer(String uri) {
        List<LayerNode> nodes = getLeftNode();
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
