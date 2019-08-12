package com.titan.jnly.map.ui.frame;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.arcgisservices.LabelDefinition;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.arcruntime.core.TocContainer;
import com.lib.bandaid.arcruntime.layer.info.Extent;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.widget.easyui.utils.WidgetUtil;
import com.lib.bandaid.widget.treeview.action.TreeView;
import com.lib.bandaid.widget.treeview.adapter.i.ITreeViewNodeListening;
import com.lib.bandaid.widget.treeview.bean.TreeNode;
import com.lib.bandaid.widget.treeview.holder.ItemFactory;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.common.uitls.ConverterUtils;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.login.ui.aty.LoginAty;
import com.titan.jnly.map.utils.NodeIteration;
import com.titan.jnly.system.Constant;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/5/23.
 */

public class FrameLayer extends BaseMapWidget implements ITreeViewNodeListening, TocContainer.ILayerLoaded, View.OnClickListener {

    private TextView tvAccount, tvRealName;
    private ImageView ivHead;

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
        tvAccount = $(R.id.tvAccount);
        tvRealName = $(R.id.tvRealName);
        ivHead = $(R.id.ivHead);


        llTreeView = $(R.id.llRoot);
    }

    @Override
    public void registerEvent() {
        ivHead.setOnClickListener(this);
    }

    @Override
    public void initClass() {
        initLayerTree();
        UserInfo info = Constant.getUserInfo();
        if (info != null) {
            tvAccount.setText(info.getUserName());
            tvRealName.setText(info.getName());
        }
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
                        setRenderArea((FeatureLayer) layer);
                    }
                    if (name.equals("古树名木单株调查")) {
                        setRenderPoint((FeatureLayer) layer);
                    }
                }
            }
        });
    }

    public void setRenderArea(FeatureLayer layer) {
        SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, simpleLineSymbol);
        layer.setRenderer(new SimpleRenderer(simpleFillSymbol));
    }

    public void setRenderPoint(FeatureLayer layer) {
        MarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 15);
        layer.setRenderer(new SimpleRenderer(markerSymbol));
        addLabel(layer);
    }


    private void addLabel(FeatureLayer featureLayer) {
        TextSymbol textSymbol = new TextSymbol();
        textSymbol.setSize(12);
        textSymbol.setColor(Color.RED);
        textSymbol.setHaloColor(0xFFFFFF00);
        textSymbol.setHaloWidth(0);
        textSymbol.setOffsetY(30);

        // 创建label字符串
        JsonObject json = new JsonObject();
        JsonObject expressionInfo = new JsonObject();
        expressionInfo.add("expression", new JsonPrimitive("$" + "feature.DCSXH"));
        json.add("labelExpressionInfo", expressionInfo);
        json.add("labelPlacement", new JsonPrimitive("esriServerPolygonPlacementAlwaysHorizontal"));
        json.add("where", new JsonPrimitive("DCSXH <> ' '"));
        json.add("symbol", new JsonParser().parse(textSymbol.toJson()));
        String labelStr = json.toString();
        // 构建LabelDefinition
        LabelDefinition labelDefinition = LabelDefinition.fromJson(labelStr);
        featureLayer.getLabelDefinitions().clear();
        featureLayer.getLabelDefinitions().add(labelDefinition);
        // 启用Label标注
        featureLayer.setLabelsEnabled(true);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivHead) {
            Intent intent = new Intent(context, LoginAty.class);
            startActivity(intent);
        }
    }
}
