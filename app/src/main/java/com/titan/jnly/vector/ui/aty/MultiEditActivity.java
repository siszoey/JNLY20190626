package com.titan.jnly.vector.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.camera.lib.ui.aty.PhotoActivity;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.TimePickerDialogUtil;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui.PropertyEditView;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.tool.SketchEditorTools;

import java.util.Map;

public class MultiEditActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private Button btnExit, btnSubmit;
    private FrameLayout flRoot;

    public static GroupItem<Feature> data;
    private PropertyView propertyView;
    private SketchEditorTools tools;
    private FeatureTable feaTable;
    private Feature feature;
    private EasyUiXml easyUiXml;

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
        propertyView = new PropertyView(this);
        flRoot.addView(propertyView);
    }

    @Override
    protected void registerEvent() {
        btnExit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        propertyView.setImgAdapter(new PropertyEditView.ImgAdapter() {
            @Override
            public String adapter(Object val) {
                return null;
            }
        });
        propertyView.setInputFace(new PropertyEditView.InputFace() {
            @Override
            public void input(View v) {
                if (v instanceof EditText) {
                    TimePickerDialogUtil.bindTimePickerFull(_context, v);
                }
                if (v instanceof ImageView) {
                    PhotoActivity.start(_context, false, Config.APP_PHOTO_PATH, true, true, null);
                }
            }
        });
    }

    @Override
    protected void initClass() {
        tools = new SketchEditorTools(ArcMap.arcMap);
        feature = data.getData();
        feaTable = ((LayerNode) data.getTag()).tryGetFeaTable();
        easyUiXml = Constant.getEasyUiXmlByName(_context, feaTable.getTableName());
        easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, feature.getAttributes());
        propertyView.setListener(new ILifeCycle() {
            @Override
            public void beforeCreate() {
                //initDefaultData(easyUiXml);
            }

            @Override
            public void afterCreate() {
                //dealInnerLogic();
            }
        }).resolutionData(easyUiXml);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnExit) {
            onBackPressed();
        }
        if (id == R.id.btnSubmit) {
            boolean verify = propertyView.verifyForm();
            if (!verify) {
                showLongToast("请将信息填写完整!");
                return;
            }
            Map map = propertyView.getForm();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (easyUiXml != null) easyUiXml.release();
    }


    @Override
    public void onBackPressed() {
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
}
