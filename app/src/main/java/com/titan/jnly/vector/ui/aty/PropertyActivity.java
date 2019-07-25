package com.titan.jnly.vector.ui.aty;

import android.location.Location;
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
import com.esri.arcgisruntime.data.Field;
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui.PropertyEditView;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.TimePickerDialogUtil;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.tool.SketchEditorTools;

import java.util.List;
import java.util.Map;

public class PropertyActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private Button btnExit, btnSubmit;
    private FrameLayout flRoot;

    public static GroupItem<Feature> data;
    private PropertyView propertyView;
    private SketchEditorTools tools;
    private FeatureTable feaTable;
    private Feature feature;
    private List<Field> fields;
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
        fields = feaTable.getFields();
        //生成模板
        //EasyUiXml easyUiXml = Resolution.convert2EasyUiXml(fields);
        //IoXml.saveOrUpDate(easyUiXml, Config.APP_PATH.concat(File.separator).concat("point.xml"));
        EasyUiXml easyUiXml = Constant.getEasyUiXmlByName(_context, feaTable.getTableName());
        easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, feature.getAttributes());
        initDefaultData(easyUiXml);
        propertyView.setData(easyUiXml).resolutionData();
    }

    private void initDefaultData(EasyUiXml easyUiXml) {
        Location location = Constant.location;
        UiXml dcrq = easyUiXml.getUiXml("DCRQ");
        if (dcrq.getValue() == null) {
            if (dcrq != null) dcrq.setValue(DateUtil.getCurrentCalendar());
            //设置经度纬度海拔
            if (location != null) {
                UiXml lon = easyUiXml.getUiXml("LON");
                UiXml lat = easyUiXml.getUiXml("LAT");
                UiXml alt = easyUiXml.getUiXml("HAIBA");
                if (lon != null) lon.setValue(location.getLongitude());
                if (lat != null) lat.setValue(location.getLatitude());
                if (alt != null) alt.setValue(location.getAltitude());
            }
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
}
