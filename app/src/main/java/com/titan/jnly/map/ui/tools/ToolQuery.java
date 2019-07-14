package com.titan.jnly.map.ui.tools;

import android.view.MotionEvent;
import android.view.View;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.geometry.Point;
import com.lib.bandaid.arcruntime.core.SelectContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.dialog.PropertyDialog;

import java.util.List;
import java.util.Map;

public class ToolQuery extends BaseTool implements SelectContainer.ICallBack {

    List<LayerNode> layerNodes;

    public ToolQuery() {
        id = getClass().getSimpleName();
        name = "识别";
        resId = R.mipmap.ic_map_query_normal;
        checkedResId = R.mipmap.ic_map_query_pressed;
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
        arcMap.getSelectContainer().clearSelection(layerNodes);
    }

    @Override
    public void viewClick(View view) {
        super.viewClick(view);
        layerNodes = arcMap.getTocContainer().getLeafLayerNodesVisible();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Point point = arcMap.screenToLocation(e);
        arcMap.getSelectContainer().queryByGeometry(layerNodes, point, this);
        return super.onSingleTapUp(e);
    }


    @Override
    public void success(Map<LayerNode, List<Feature>> res) {
        if (res == null || res.size() == 0) return;
        for (LayerNode node : res.keySet()) {
            List<Feature> features = res.get(node);
            if (features == null || features.size() == 0) continue;
            FeatureTable feaTable = node.tryGetFeaTable();
            List<Field> fields = null;
            if (feaTable != null) {
                fields = feaTable.getFields();
            }
            PropertyDialog.newInstance(node.getName(), fields, features.get(0).getAttributes()).show(context);
            break;
        }
    }

    @Override
    public void fail(Exception e) {

    }
}
