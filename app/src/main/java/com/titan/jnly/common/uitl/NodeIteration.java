package com.titan.jnly.common.uitl;

import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.widget.treeview.action.TreeView;
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
            boolean hasInVisible = layerNode.hasInVisible();
            treeNode.setSelected(!hasInVisible);
        }
        List<LayerNode> list = layerNode.getNodes();
        if (list == null) return treeNode;

        LayerNode _layerNode;
        TreeNode _treeNode;
        for (int i = 0; i < list.size(); i++) {
            _layerNode = list.get(i);
            //System.out.println("---------" + _layerNode.getName() + "---------");
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
            //System.out.println("---------" + _layerNode.getName() + "---------");
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


    public static void iteration(TreeView treeView, TreeNode root, LayerNode node) {
        if (node.isLocal()) {
            List<TreeNode> treeNodes = treeView.getAllNodes();
            if (treeNodes == null) return;
            boolean hasNode = false;
            TreeNode treeNode = null;
            for (int i = 0; i < treeNodes.size(); i++) {
                treeNode = treeNodes.get(i);
                LayerNode layerNode = (LayerNode) treeNode.getValue();
                if (layerNode == null) continue;
                System.out.println(">>:" + layerNode.getUri());
                System.out.println("<<:" + node.getUri());
                if (layerNode.getUri().equals(node.getUri())) {
                    hasNode = true;
                }
            }
            if (!hasNode) {
                treeNode = new TreeNode(node);
                treeNode.setLabel(node.getName());
                treeNode.setValue(node);
                treeNode.setExpanded(false);
                treeNode.setItemClickEnable(true);
                treeNode.setSelected(node.getVisible());

                treeView.addNode(root, treeNode);
            } else {

                treeNode = new TreeNode(node);
                treeNode.setLabel(node.getName());
                treeNode.setValue(node);
                treeNode.setExpanded(false);
                treeNode.setItemClickEnable(true);
                treeNode.setSelected(node.getVisible());

                treeNode.addChild(treeNode);
            }
        } else {
            TreeNode treeNode = iteration(node, TreeNode.level(1));
            treeView.addNode(root, treeNode);
        }
    }
}
