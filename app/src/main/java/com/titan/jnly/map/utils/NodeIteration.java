package com.titan.jnly.map.utils;

import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.widget.treeview.bean.TreeNode;

import java.util.List;

/**
 * Created by zy on 2019/5/24.
 */

public final class NodeIteration {

    public static TreeNode iteration(LayerNode layerNode, TreeNode treeNode) {
        if (layerNode == null || treeNode == null) return treeNode;
        {
            treeNode.setLabel(layerNode.getName());
            treeNode.setValue(layerNode);
            treeNode.setExpanded(false);
            treeNode.setItemClickEnable(true);
            treeNode.setSelected(!layerNode.hasInVisible());
        }
        List<LayerNode> list = layerNode.getNodes();
        if (list == null) return treeNode;

        LayerNode _layerNode;
        TreeNode _treeNode;
        for (int i = 0; i < list.size(); i++) {
            _layerNode = list.get(i);
            System.out.println("---------" + _layerNode.getName() + "---------");

            _treeNode = new TreeNode(_layerNode);
            _treeNode.setLabel(_layerNode.getName());
            _treeNode.setExpanded(false);
            _treeNode.setItemClickEnable(true);
            _treeNode.setLevel(treeNode.getLevel() + 1);
            _treeNode.setSelected(_layerNode.getVisible());
            treeNode.addChild(_treeNode);

            _iteration(_layerNode, _treeNode);
        }
        return treeNode;
    }


    private static TreeNode _iteration(LayerNode layerNode, TreeNode treeNode) {
        if (layerNode == null || treeNode == null) return treeNode;
        {
            treeNode.setLabel(layerNode.getName());
            treeNode.setValue(layerNode);
            treeNode.setExpanded(false);
            treeNode.setItemClickEnable(true);
            treeNode.setSelected(layerNode.getVisible());
        }
        List<LayerNode> list = layerNode.getNodes();
        if (list == null) return treeNode;

        LayerNode _layerNode;
        TreeNode _treeNode;
        for (int i = 0; i < list.size(); i++) {
            _layerNode = list.get(i);
            System.out.println("---------" + _layerNode.getName() + "---------");

            _treeNode = new TreeNode(_layerNode);
            _treeNode.setLabel(_layerNode.getName());
            _treeNode.setExpanded(false);
            _treeNode.setItemClickEnable(true);
            _treeNode.setLevel(treeNode.getLevel() + 1);
            _treeNode.setSelected(_layerNode.getVisible());
            treeNode.addChild(_treeNode);

            _iteration(_layerNode, _treeNode);
        }
        return treeNode;
    }

}
