package com.titan.jnly.map.ui.frame;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.arcruntime.core.TocContainer;
import com.lib.bandaid.arcruntime.layer.info.Extent;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.widget.treeview.action.TreeView;
import com.lib.bandaid.widget.treeview.adapter.i.ITreeViewNodeListening;
import com.lib.bandaid.widget.treeview.bean.TreeNode;
import com.lib.bandaid.widget.treeview.holder.ItemFactory;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.map.utils.NodeIteration;

import java.io.File;
import java.util.List;
import java.util.Map;

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
        //Map<String, List<File>> tree = FileUtil.fileTrees(Config.APP_MAP_SPATIAL, "geodatabase");
        //arcMap.setBaseMapUrl(Config.APP_ARC_MAP_BASE);
        //arcMap.setMapServerUrl(Config.APP_ARC_MAP_SERVICE, Config.APP_ARC_MAP_SERVICE_2015_SS);
        //arcMap.setMapServerDesc("图层1", "图层2");
        arcMap.setMapLocalUrl(Config.APP_SDB_PATH);
        arcMap.getTocContainer().addILayerLoaded(this);
        arcMap.mapLoad(new ArcMap.IMapReady() {
            @Override
            public void onMapReady() {
                LayerList list = arcMap.getMapView().getMap().getOperationalLayers();
                for (Layer layer : list) {
                    String name = layer.getName();
                    if (name.equals("古树群调查表")) {
                        setRender((FeatureLayer) layer);
                    }
                }
            }
        });
    }

    public static void setRender(FeatureLayer layer) {
        SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, simpleLineSymbol);
        layer.setRenderer(new SimpleRenderer(simpleFillSymbol));
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
        //移动到数据范围
        if (treeNode.getValue() != null) {
            LayerContent layerContent = ((LayerNode) treeNode.getValue()).getLayerContent();
            if (layerContent instanceof FeatureLayer) {
                Envelope extent = ((FeatureLayer) layerContent).getFeatureTable().getExtent();
                arcMap.getMapControl().zoomG(extent);
            }
        }
    }

    @Override
    public void nodeLongClickListening(TreeNode treeNode) {

    }


    void initLayerTree() {
        if (treeView != null) treeView.reMoveAllNode();
        nodeProject = TreeNode.root();
        treeRoot = TreeNode.level(0).setLabel("图层").setExpanded(true).setItemClickEnable(true);
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
