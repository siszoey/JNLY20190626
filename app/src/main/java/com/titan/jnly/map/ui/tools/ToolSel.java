package com.titan.jnly.map.ui.tools;

import android.view.MotionEvent;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.lib.bandaid.arcruntime.core.QueryContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.lib.bandaid.arcruntime.util.FeatureTaker;
import com.lib.bandaid.arcruntime.util.FeatureUtil;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.frame.FrameQuery;

import java.util.List;

/**
 * Created by zy on 2019/5/28.
 */

public class ToolSel extends BaseTool {

    public ToolSel() {
        id = getClass().getSimpleName();
        name = "选择";
        resId = R.mipmap.tool_map_identify_nomal;
        checkedResId = R.mipmap.tool_map_identify_pressed;
    }

    @Override
    public boolean isCheckBtn() {
        return true;
    }

    @Override
    public void activate() {
        super.activate();
        if (isRegisterMapEvent) {
            // arcMap.getSelContainer().deactivate();
        }
    }

    @Override
    public void deactivate() {
        super.deactivate();
        if (isRegisterMapEvent) {
            // arcMap.getSelContainer().deactivate();
        }
    }

    FrameQuery frameQuery;

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Point point = arcMap.getMapView().screenToLocation(new android.graphics.Point((int) e.getX(), (int) e.getY()));
        queryByGeometry(point);
        frameQuery = (FrameQuery) arcMap.getWidgetContainer().getWidget(FrameQuery.class);
        frameQuery.show();
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
    }


    public void queryByGeometry(Geometry geometry) {
        List<LayerNode> nodes = arcMap.getTocContainer().getLayerNodes();
        List<LayerNode> leftNodes;
        QueryParameters queryParameters;
        for (LayerNode node : nodes) {
            leftNodes = node.getLeafNode();
            for (LayerNode leftNode : leftNodes) {
                if (!leftNode.getVisible() || !leftNode.isValid()) continue;
                queryParameters = new QueryParameters();
                queryParameters.setGeometry(geometry);
                arcMap.getQueryContainer().queryFeatures(leftNode, queryParameters, new QueryContainer.ICallBack() {
                    @Override
                    public void ready() {

                    }

                    @Override
                    public void success(List<Feature> features) {
                        List<FeatureTaker<LayerNode>> list = FeatureUtil.convertFeatureTaker(features);
                        for (FeatureTaker taker : list) {
                            taker.setData(leftNode);
                        }
                        frameQuery.appendFeatures(list);
                    }

                    @Override
                    public void fail(Exception e) {
                        e.printStackTrace();
                        System.out.println(leftNode.getUri());
                    }
                });
            }
        }
    }
}
