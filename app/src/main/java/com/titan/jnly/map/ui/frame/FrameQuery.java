package com.titan.jnly.map.ui.frame;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.arcruntime.layer.info.LayerInfo;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.util.FeatureTaker;
import com.lib.bandaid.widget.base.EGravity;
import com.lib.bandaid.widget.layout.RootStatusView;
import com.titan.jnly.R;
import com.titan.jnly.map.apt.SelFeatureApt;
import com.titan.jnly.map.ui.dialog.PropertyDialog;

import java.util.List;

/**
 * Created by zy on 2019/5/29.
 */

public class FrameQuery extends BaseMapWidget implements View.OnClickListener, BaseRecycleAdapter.IViewClickListener<FeatureTaker<LayerNode>> {

    RootStatusView root;
    ImageView ivClose;
    RecyclerView rvFeature;
    SelFeatureApt selFeatureApt;

    public FrameQuery(Context context) {
        super(context);
        w = 0.2f;
        h = 1;
        layoutGravity = EGravity.LEFT_CENTER.getValue();
        setContentView(R.layout.map_ui_frame_query);
    }

    @Override
    public void initialize() {
        ivClose = $(R.id.ivClose);
        rvFeature = $(R.id.rvFeature);
        root = (RootStatusView) view;
    }

    @Override
    public void registerEvent() {
        ivClose.setOnClickListener(this);
    }

    @Override
    public void initClass() {
        selFeatureApt = new SelFeatureApt(rvFeature);
        selFeatureApt.setIViewClickListener(this);
    }

    public void appendFeatures(List<FeatureTaker<LayerNode>> features) {
        root.showContent();
        selFeatureApt.appendList(features);
    }

    @Override
    public void show() {
        super.show();
        selFeatureApt.removeAll();
        root.showLoading();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivClose) {
            hide();
        }
    }

    @Override
    public void onClick(View view, FeatureTaker<LayerNode> data, int position) {
        LayerNode node = data.getData();
        LayerInfo info = node.getInfo();
        PropertyDialog.newInstance(data.getData().getName(), info, data.getFeature().getAttributes()).show(context);
    }
}
