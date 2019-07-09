package com.titan.jnly.map.ui.frame;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;

import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Polyline;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.arcruntime.core.draw.DrawType;
import com.lib.bandaid.arcruntime.core.draw.ValueCallback;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.widget.base.EGravity;
import com.titan.jnly.R;
import com.titan.jnly.vector.bean.ActionModel;
import com.titan.jnly.vector.tool.SketchEditorTools;

import java.util.List;

public class VectorBar extends BaseMapWidget implements View.OnClickListener {

    RadioButton rb_xbbj;

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
    }

    @Override
    public void registerEvent() {
        rb_xbbj.setOnClickListener(this);
    }

    @Override
    public void initClass() {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == rb_xbbj.getId()) {
            actionModel = ActionModel.ADDFEATURE;
            arcMap.getSketchTool().setCallBack(new ValueCallback() {
                @Override
                public void onGeometry(Geometry geometry) {
                    if(actionModel == ActionModel.ADDFEATURE){
                        List<LayerNode> layerNodes = arcMap.getTocContainer().getLeafLayerNodesVisible();
                        FeatureTable table = layerNodes.get(0).tryGetFeaTable();
                        tools.addLineToLayer(table,(Polyline) geometry,null);
                    }
                }
            });
            arcMap.getSketchTool().activate(DrawType.FREEHAND_POLYLINE);


        }
    }
}
