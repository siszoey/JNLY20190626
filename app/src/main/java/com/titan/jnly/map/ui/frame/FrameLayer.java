package com.titan.jnly.map.ui.frame;

import android.content.Context;
import android.widget.LinearLayout;

import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.arcruntime.core.TocContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.widget.treeview.action.TreeView;
import com.lib.bandaid.widget.treeview.adapter.i.ITreeViewNodeListening;
import com.lib.bandaid.widget.treeview.bean.TreeNode;
import com.lib.bandaid.widget.treeview.holder.ItemFactory;
import com.titan.jnly.R;
import com.titan.jnly.map.utils.NodeIteration;

/**
 * Created by zy on 2019/5/23.
 */

public class FrameLayer extends BaseMapWidget implements ITreeViewNodeListening, TocContainer.ILayerLoaded {

    private LinearLayout llTreeView;
    private TreeView treeView;
    private TreeNode treeRoot;
    private TreeNode nodeProject;

    public FrameLayer(Context context) {
        super(context);
        layoutGravity = -1;
        setContentView(R.layout.map_ui_frame_layer);
    }

    @Override
    public void initialize() {
        llTreeView = $(R.id.llRoot);
    }

    @Override
    public void registerEvent() {

    }

    @Override
    public void initClass() {
        initLayerTree();
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        loadMap();
    }


    void loadMap() {
        //arcMap.setBaseMapUrl(Config.APP_ARC_MAP_BASE);
        //arcMap.setMapServerUrl(Config.APP_ARC_MAP_SERVICE, Config.APP_ARC_MAP_SERVICE_2015_SS);
        //arcMap.setMapServerDesc("图层1", "图层2");
        arcMap.getTocContainer().addILayerLoaded(this);
        arcMap.mapLoad(new ArcMap.IMapReady() {
            @Override
            public void onMapReady() {
            }
        });
    }

    @Override
    public void iLayerLoaded(LayerNode node) {
        TreeNode treeNode = NodeIteration.iteration(node, TreeNode.level(1));
        treeView.addNode(treeRoot, treeNode);
    }

    @Override
    public void nodeCheckListening(boolean checked, TreeNode treeNode) {
        if (treeNode.getValue() != null) {
            ((LayerNode) treeNode.getValue()).setVisible(checked);
        }
    }

    @Override
    public void nodeClickListening(TreeNode treeNode) {
        System.out.println(1122);
    }

    @Override
    public void nodeLongClickListening(TreeNode treeNode) {
        System.out.println(1122);
    }


    void initLayerTree() {
        if (treeView != null) treeView.reMoveAllNode();
        nodeProject = TreeNode.root();
        treeRoot = TreeNode.level(0).setLabel("一张图图层").setExpanded(true).setItemClickEnable(true);
        treeRoot.setLevel(0);
        treeRoot.setExpanded(true);
        treeRoot.setItemClickEnable(true);
        nodeProject.addChild(treeRoot);
        treeView = new TreeView(nodeProject, context, new ItemFactory());
        treeView.setITreeViewNodeListening(this);
        llTreeView.removeAllViews();
        llTreeView.addView(treeView.getView());
    }

}
