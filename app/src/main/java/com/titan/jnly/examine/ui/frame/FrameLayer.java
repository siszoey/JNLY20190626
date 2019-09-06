package com.titan.jnly.examine.ui.frame;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.arcruntime.core.TocContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.util.ViewUtil;
import com.lib.bandaid.widget.treeview.action.TreeView;
import com.lib.bandaid.widget.treeview.adapter.i.ITreeViewNodeListening;
import com.lib.bandaid.widget.treeview.bean.TreeNode;
import com.lib.bandaid.widget.treeview.holder.ItemFactory;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.common.mvp.DicHandle;
import com.titan.jnly.common.uitl.NodeIteration;
import com.titan.jnly.examine.ui.aty.DataListAty;
import com.titan.jnly.invest.ui.aty.InvestActivity;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.login.ui.aty.LoginAty;
import com.titan.jnly.system.Constant;

/**
 * Created by zy on 2019/5/23.
 */

public class FrameLayer extends BaseMapWidget
        implements ITreeViewNodeListening,
        TocContainer.ILayerLoaded,
        View.OnClickListener {

    private TextView tvAccount, tvRealName;
    private ImageView ivHead;

    private TextView tvData;

    private LinearLayout llTreeView;
    private TreeView treeView;
    private TreeNode treeRoot;
    private TreeNode nodeProject;

    private TextView tvSync;

    public FrameLayer(Context context) {
        super(context);
        layoutGravity = ViewUtil.MATCH_PARENT;
        setContentView(R.layout.exam_ui_menu_layout);
    }

    @Override
    public void initialize() {
        tvAccount = $(R.id.tvAccount);
        tvRealName = $(R.id.tvRealName);
        ivHead = $(R.id.ivHead);
        tvData = $(R.id.tvData);
        llTreeView = $(R.id.llRoot);
        tvSync = $(R.id.tvSync);
    }

    @Override
    public void registerEvent() {
        ivHead.setOnClickListener(this);
        tvSync.setOnClickListener(this);
        tvData.setOnClickListener(this);
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
        arcMap.setMapServerUrl(Config.BASE_URL.Fea_MapService);
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
            startActivity(new Intent(context, LoginAty.class));
        }
        if (v.getId() == R.id.tvSync) {
            DicHandle.create(context).reqDic(Constant.getUser());
        }
        if (v.getId() == R.id.tvData) {
            startActivity(new Intent(context, DataListAty.class));
        }
    }
}
