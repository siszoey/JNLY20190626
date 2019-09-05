package com.titan.jnly.common.tools;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.SelectContainer;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.lib.bandaid.arcruntime.tools.extend.ToolSelExtend;
import com.lib.bandaid.arcruntime.util.FeatureUtil;
import com.lib.bandaid.util.VibratorUtil;
import com.titan.jnly.R;
import com.titan.jnly.invest.ui.dialog.FeatureDialog;
import com.titan.jnly.invest.ui.dialog.PropertyDialog;

import java.util.List;
import java.util.Map;

public class ToolQuery extends BaseTool implements SelectContainer.ICallBack {

    List<LayerNode> layerNodes;
    private boolean isLongPress;
    private ToolSelExtend extend;

    public ToolQuery() {
        id = getClass().getSimpleName();
        name = "识别";
        resId = R.mipmap.ic_map_query_normal;
        checkedResId = R.mipmap.ic_map_query_pressed;
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        extend = ToolSelExtend.create(arcMap);
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
    public boolean onTouchStart(MotionEvent motionEvent) {
        System.out.println("onTouchStart");
        isLongPress = false;
        return super.onTouchStart(motionEvent);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        System.out.println("onLongPress");
        VibratorUtil.vibrate(context, 120);
        isLongPress = true;
        extend.drawType(ToolSelExtend.Type.rect).startDraw(new PointF(e.getX(), e.getY()));
        super.onLongPress(e);
    }

    @Override
    public boolean onTouchMoving(MotionEvent motionEvent) {
        System.out.println("onTouchMoving");
        if (isLongPress) {
            extend.underDraw(new PointF(motionEvent.getX(), motionEvent.getY()));
            return true;
        }
        return super.onTouchMoving(motionEvent);
    }

    @Override
    public boolean onTouchCancel(MotionEvent motionEvent) {
        System.out.println("onTouchCancel");
        if (isLongPress) {
            extend.cancelDraw(new PointF(motionEvent.getX(), motionEvent.getY()));
            isLongPress = false;

            Geometry geometry = extend.getGeometry();
            arcMap.getSelectContainer().queryByGeometry(layerNodes, geometry, this);
        }
        return super.onTouchCancel(motionEvent);
    }

    @Override
    public void success(Map<LayerNode, List<Feature>> res) {
        if (res == null || res.size() == 0) return;
        List<Feature> features = FeatureUtil.mapConvertList(res);
        if (features.size() == 0) return;
        if (features.size() == 1) {
            for (LayerNode node : res.keySet()) {
                features = res.get(node);
                if (features == null || features.size() == 0) continue;
                List<Field> fields = node.tryGetFields();
                Map map = features.get(0).getAttributes();
                PropertyDialog.newInstance(node.getName(), fields, map).show(context);
                break;
            }
        } else {
            FeatureDialog.newInstance().initData("选择古树", res, new FeatureDialog.ICallBack() {
                @Override
                public void callBack(GroupItem<Feature> data) {
                    LayerNode node = (LayerNode) data.getTag();
                    List<Field> fields = node.tryGetFields();
                    PropertyDialog.newInstance(node.getName(), fields, data.getData().getAttributes()).show(context);
                }
            }).show(context);
        }
    }

    @Override
    public void fail(Exception e) {

    }
}
