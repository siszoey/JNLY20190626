package com.titan.jnly.map.ui.tools;

import android.view.MotionEvent;
import android.view.View;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Point;
import com.lib.bandaid.arcruntime.core.QueryContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.dialog.PropertyDialog;

import java.util.List;

public class ToolQuery extends BaseTool implements QueryContainer.ICallBack {

    LayerNode layerNode;

    public ToolQuery() {
        id = getClass().getSimpleName();
        name = "识别";
        resId = R.drawable.ic_map_query;
        checkedResId = R.drawable.ic_tool_pressed;
    }

    @Override
    public boolean isCheckBtn() {
        return true;
    }

    @Override
    public void activate() {
        super.activate();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        arcMap.getSelectContainer().clearSelection(layerNode);
    }

    @Override
    public void viewClick(View view) {
        super.viewClick(view);
        List<LayerNode> layerNodes = arcMap.getTocContainer().getLeafLayerNodesVisible();
        layerNode = layerNodes.get(0);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Point point = arcMap.screenToLocation(e);
        arcMap.getSelectContainer().queryByGeometry(layerNode, point, this);
        return super.onSingleTapUp(e);
    }

    @Override
    public void ready() {

    }

    @Override
    public void success(List<Feature> features) {
        if (features == null || features.size() == 0) return;
        PropertyDialog.newInstance(layerNode.getName(), null, features.get(0).getAttributes()).show(context);
    }

    @Override
    public void fail(Exception e) {

    }
}
