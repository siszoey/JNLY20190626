package com.titan.jnly.vector.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.Field;
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.arcruntime.wiget.MapLayoutView;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.TimePickerDialogUtil;
import com.titan.jnly.R;
import com.titan.jnly.vector.tool.SketchEditorTools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private Button btnExit, btnSubmit;
    private FrameLayout flRoot;

    public static GroupItem<Feature> data;
    private MapLayoutView entityLayoutView;
    private SketchEditorTools tools;
    private FeatureTable feaTable;
    private Feature feature;
    private List<Field> fields;
    private Map property;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, "属性编辑", Gravity.CENTER);
        setContentView(R.layout.vector_ui_aty_property_layout);
    }

    @Override
    protected void initialize() {
        flRoot = $(R.id.flRoot);
        btnExit = $(R.id.btnExit);
        btnSubmit = $(R.id.btnSubmit);
        entityLayoutView = new MapLayoutView(this);
        entityLayoutView.setMode(MapLayoutView.Mode.READ_WRITE);
        flRoot.addView(entityLayoutView);
    }

    @Override
    protected void registerEvent() {
        btnExit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        entityLayoutView.setInputFace(new MapLayoutView.InputFace() {
            @Override
            public void input(View v) {
                TimePickerDialogUtil.bindTimePickerFull(_context, v);
            }
        });
    }

    @Override
    protected void initClass() {
        tools = new SketchEditorTools(ArcMap.arcMap);
        feature = data.getData();
        feaTable = ((LayerNode) data.getTag()).tryGetFeaTable();
        fields = feaTable.getFields();
        property = data.getData().getAttributes();
        if (fields != null) {
            Map<Field, Object> _property = new HashMap<>();
            Field field;
            boolean equal = false;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                for (Object name : property.keySet()) {
                    equal = ObjectUtil.baseTypeIsEqual(name, field.getName());
                    if (equal) {
                        _property.put(field, property.get(name));
                        break;
                    }
                }
            }
            entityLayoutView.setData(_property).resolutionData();
        } else {
            entityLayoutView.setData(property).resolutionData();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnExit) {
            new ATEDialog.Theme_Alert(_context)
                    .title("提示")
                    .content("确认退出？")
                    .positiveText("退出")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).show();
        }
        if (id == R.id.btnSubmit) {
            Map map = entityLayoutView.getForm();
            map.remove("OBJECTID");
            feature.getAttributes().putAll(map);
            tools.updateFeature(feaTable, feature, new SketchEditorTools.ICallBack() {
                @Override
                public void ok() {
                    finish();
                }
            });
        }
    }
}
