package com.titan.jnly.map.ui.tools;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.geometry.Geometry;
import com.lib.bandaid.arcruntime.core.QueryContainer;
import com.lib.bandaid.arcruntime.core.draw.ValueCallback;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.lib.bandaid.arcruntime.util.FeatureTaker;
import com.lib.bandaid.arcruntime.util.FeatureUtil;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.dialog.DrawDialog;
import com.titan.jnly.map.ui.frame.FrameQuery;

import java.util.List;

/**
 * Created by zy on 2019/5/28.
 */

public class QuerySel extends BaseTool {

    FrameQuery frameQuery;

    public QuerySel() {
        id = getClass().getSimpleName();
        name = "查询";
        resId = R.mipmap.tool_map_identify_nomal;
        checkedResId = R.mipmap.tool_map_identify_pressed;
        isRegisterMapEvent = false;
    }

    @Override
    public boolean isCheckBtn() {
        return true;
    }

    @Override
    public void activate() {
        super.activate();

        DrawDialog.newInstance(new DrawDialog.ICallBack() {
            @Override
            public void callback(Object o) {
                arcMap.getSketchTool().setCallBack(new ValueCallback() {
                    @Override
                    public void onGeometry(Geometry geometry) {
                        queryByGeometry(geometry);
                        frameQuery = (FrameQuery) arcMap.getWidgetContainer().getWidget(FrameQuery.class);
                        frameQuery.show();
                    }
                });
                arcMap.getSketchTool().activate((int) o);
            }
        }).show(context);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        arcMap.getSketchTool().deactivate();
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
