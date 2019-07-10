package com.titan.jnly.map.ui.frame;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.arcruntime.core.draw.DrawType;
import com.lib.bandaid.arcruntime.core.draw.ValueCallback;
import com.lib.bandaid.arcruntime.event.IArcMapEvent;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.ToastUtil;
import com.lib.bandaid.widget.base.EGravity;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.dialog.FeatureDialog;
import com.titan.jnly.map.ui.dialog.LayerDialog;
import com.titan.jnly.vector.bean.ActionModel;
import com.titan.jnly.vector.tool.SketchEditorTools;

import java.util.List;
import java.util.Map;

public class VectorBar extends BaseMapWidget implements View.OnClickListener, IArcMapEvent {
    //新增
    RadioButton rb_xbbj;
    //选择
    RadioButton rb_xzxb;
    //平移
    RadioButton rb_py;
    //删除
    RadioButton rb_xbsc;
    //编辑
    RadioButton rb_sxbj;
    //分割
    RadioButton rb_fgxb;
    //复制
    RadioButton rb_xbfz;

    ActionModel actionModel;
    SketchEditorTools tools;

    public VectorBar(Context context) {
        super(context);
        w = 0.7f;
        h = -1f;
        layoutGravity = EGravity.BOTTOM_CENTER.getValue();
        setContentView(R.layout.include_feature_tools);
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        tools = new SketchEditorTools(arcMap);
    }

    @Override
    public void initialize() {
        rb_xbbj = $(R.id.rb_xbbj);
        rb_xzxb = $(R.id.rb_xzxb);
        rb_py = $(R.id.rb_py);
        rb_xbsc = $(R.id.rb_xbsc);
        rb_sxbj = $(R.id.rb_sxbj);
        rb_fgxb = $(R.id.rb_fgxb);
        rb_xbfz = $(R.id.rb_xbfz);
    }

    @Override
    public void registerEvent() {
        rb_xbbj.setOnClickListener(this);
        rb_xzxb.setOnClickListener(this);
        rb_py.setOnClickListener(this);
        rb_xbsc.setOnClickListener(this);
        rb_sxbj.setOnClickListener(this);
        rb_fgxb.setOnClickListener(this);
        rb_xbfz.setOnClickListener(this);
    }

    @Override
    public void initClass() {

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == rb_xbbj.getId()) {
            addFeature();
        }
        //申请地图事件
        if (id == rb_xzxb.getId()) {
            arcMap.setEvent(this);
        }
        if (id == rb_py.getId()) {
            clearAll();
        }
        if (id == rb_xbsc.getId()) {
            delFeature();
        }
        if (id == rb_sxbj.getId()) {
            editProperty();
        }
        if (id == rb_fgxb.getId()) {
            spitFeature();
        }
        if (id == rb_xbfz.getId()) {
            copyGeometry();
        }
    }

    /**
     * 新增图斑
     */
    void addFeature() {
        List<LayerNode> list = arcMap.getTocContainer().getLeafLayerNodesVisible();
        LayerDialog.newInstance().setCallBack(list, new LayerDialog.ICallBack() {
            @Override
            public void callBack(LayerNode layerNode) {
                actionModel = ActionModel.ADDFEATURE;
                arcMap.getSketchTool().setCallBack(new ValueCallback() {
                    @Override
                    public void onGeometry(Geometry geometry) {
                        if (actionModel == ActionModel.ADDFEATURE) {
                            FeatureTable table = layerNode.tryGetFeaTable();
                            if (table == null) return;
                            tools.addLineToLayer(table, (Polyline) geometry, null);
                        }
                    }
                });
                arcMap.getSketchTool().activate(DrawType.FREEHAND_POLYLINE);
            }
        }).show(context);
    }

    /**
     * 选择图斑
     *
     * @param geometry
     */
    void selFeature(Geometry geometry) {
        List<LayerNode> layerNodes = arcMap.getTocContainer().getLeafLayerNodesVisible();
        arcMap.getSelectContainer().setting(FeatureLayer.SelectionMode.NEW).queryByGeometry(layerNodes, geometry);
    }

    /**
     * 删除选择集里的数据
     */
    void delFeature() {
        Map<LayerNode, List<Feature>> map = arcMap.getSelectContainer().getHasDataResult();
        if (map == null) {
            ToastUtil.showLong(context, "没有找到要删除的数据！");
        } else {
            FeatureDialog.newInstance().initData("选择删除要素", map, new FeatureDialog.ICallBack() {
                @Override
                public void callBack(GroupItem<Feature> data) {
                    new ATEDialog.Theme_Alert(context)
                            .title("提示")
                            .content("确认删除？")
                            .positiveText("删除")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    LayerNode layerNode = (LayerNode) data.getTag();
                                    if (layerNode == null) return;
                                    FeatureTable feaTable = layerNode.tryGetFeaTable();
                                    Feature feature = data.getData();
                                    tools.delFeature(feaTable, feature);
                                }
                            }).show();
                }
            }).show(context);
        }
    }

    /**
     * 编辑属性
     */
    private void editProperty() {
        Map<LayerNode, List<Feature>> map = arcMap.getSelectContainer().getHasDataResult();
        if (map == null) {
            ToastUtil.showLong(context, "没有找到要编辑的数据！");
        } else {
            int count = arcMap.getSelectContainer().hasDataCount();
            if (count == 1) {

            } else if (count > 1) {
                FeatureDialog.newInstance().initData("选择要编辑的要素", map, new FeatureDialog.ICallBack() {
                    @Override
                    public void callBack(GroupItem<Feature> data) {
                        LayerNode layerNode = (LayerNode) data.getTag();
                        if (layerNode == null) return;
                        FeatureTable feaTable = layerNode.tryGetFeaTable();
                        Feature feature = data.getData();
                        // tools.delFeature(feaTable, feature);
                    }
                }).show(context);
            } else {
                ToastUtil.showLong(context, "没有找到要编辑的数据！");
            }
        }
    }

    /**
     * 编辑图形
     */
    private void editGeometry() {
        Map<LayerNode, List<Feature>> map = arcMap.getSelectContainer().getHasDataResult();
        FeatureDialog.newInstance().initData("选择要修斑的要素", map, new FeatureDialog.ICallBack() {
            @Override
            public void callBack(GroupItem<Feature> data) {
                arcMap.getSketchTool().activate(DrawType.FREEHAND_POLYLINE);
                arcMap.getSketchTool().setCallBack(new ValueCallback() {
                    @Override
                    public void onGeometry(Geometry geometry) {
                        new ATEDialog.Theme_Alert(context)
                                .title("提示")
                                .content("确认修斑？")
                                .positiveText("分割")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        //arcMap.getSketchTool().
                                    }
                                }).show();
                    }
                });
            }
        }).show(context);
    }

    public void spitFeature() {
        Map<LayerNode, List<Feature>> map = arcMap.getSelectContainer().getHasDataResult();
        FeatureDialog.newInstance().initData("选择要分割的要素", map, new FeatureDialog.ICallBack() {
            @Override
            public void callBack(GroupItem<Feature> data) {
                arcMap.getSketchTool().activate(DrawType.FREEHAND_POLYLINE);
                arcMap.getSketchTool().setCallBack(new ValueCallback() {
                    @Override
                    public void onGeometry(Geometry geometry) {
                        new ATEDialog.Theme_Alert(context)
                                .title("提示")
                                .content("确认分割？")
                                .positiveText("分割")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        LayerNode layerNode = (LayerNode) data.getTag();
                                        if (layerNode == null) return;
                                        FeatureTable feaTable = layerNode.tryGetFeaTable();
                                        Feature feature = data.getData();
                                        tools.cutFeature(feaTable, geometry, feature);
                                    }
                                }).show();
                    }
                });
            }
        }).show(context);
    }

    /**
     * 复制图形
     */
    private void copyGeometry() {
        Map<LayerNode, List<Feature>> map = arcMap.getSelectContainer().getHasDataResult();
        FeatureDialog.newInstance().initData("选择要复制的要素", map, new FeatureDialog.ICallBack() {
            @Override
            public void callBack(GroupItem<Feature> data) {
                List<LayerNode> list = arcMap.getTocContainer().getLeafLayerNodesVisible();
                list.remove(data.getTag());
                LayerDialog.newInstance().setCallBack(list, new LayerDialog.ICallBack() {
                    @Override
                    public void callBack(LayerNode layerNode) {

                        new ATEDialog.Theme_Alert(context)
                                .title("提示")
                                .content("确认复制？")
                                .positiveText("复制")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (layerNode == null) return;
                                        FeatureTable feaTable = layerNode.tryGetFeaTable();
                                        Feature feature = data.getData();
                                        tools.addGeometry(feaTable, feature.getGeometry(), null, "");
                                    }
                                }).show();


                    }
                }).show(context);
            }
        }).show(context);
    }

    /**
     * 回复默认状态
     */
    private void clearAll() {
        arcMap.setEvent(null);
        List<LayerNode> layerNodes = arcMap.getTocContainer().getLeafLayerNodes();
        arcMap.getSelectContainer().clearSelection(layerNodes);
    }

    private void activate() {

    }

    private void deactivate() {
        clearAll();
    }

    @Override
    public void show() {
        super.show();
        activate();
    }

    @Override
    public void hide() {
        super.hide();
        deactivate();
    }

    /**
     * 注册地图事件
     * *********************************************************************************************
     * *********************************************************************************************
     * *********************************************************************************************
     */


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTouchDrag(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onMultiPointerTap(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onRotate(MotionEvent event, double rotationAngle) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        selFeature(arcMap.screenToLocation(e));
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchStart(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchMoving(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchCancel(MotionEvent motionEvent) {
        return false;
    }
}
