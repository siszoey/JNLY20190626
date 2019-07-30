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
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.NumberUtil;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.SimpleMap;
import com.lib.bandaid.utils.TimePickerDialogUtil;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui_v1.ComplexTextView;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.utils.WidgetUtil;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.bean.District;
import com.titan.jnly.vector.enums.DataStatus;
import com.titan.jnly.vector.tool.SketchEditorTools;
import com.titan.jnly.vector.util.TreeModeUtil;

import java.util.List;
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
        propertyView.setImgAdapter(new PropertyView.ImgAdapter() {
            @Override
            public String adapter(Object val) {
                return null;
            }
        });
        propertyView.setInputFace(new PropertyView.InputFace() {
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

        propertyView.setListAdapter(new PropertyView.ListAdapter() {

            @Override
            public List<ItemXml> listAdapter(UiXml uiXml) {
                List<ItemXml> data = null;
                UiXml city = propertyView.getUiXmlByKey("XIAN");
                UiXml county = propertyView.getUiXmlByKey("XIANG");
                Map fields = new SimpleMap<>().push("areaCode", "code").push("areaName", "value");
                if (uiXml.getCode().equals("XIAN")) {
                    String where = " where length(f_code) = 6";
                    List<District> list = DbManager.createDefault().getListTByWhere(District.class, where);
                    data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
                }
                if (uiXml.getCode().equals("XIANG")) {
                    Object val = city.getViewCode();
                    String where = " where length(f_code) = 9 and substr(f_code,1,6) = '" + val + "'";
                    List<District> list = DbManager.createDefault().getListTByWhere(District.class, where);
                    data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
                }
                if (uiXml.getCode().equals("CUN")) {
                    Object val = county.getViewCode();
                    String where = " where length(f_code) = 12 and substr(f_code,1,9) = '" + val + "'";
                    List<District> list = DbManager.createDefault().getListTByWhere(District.class, where);
                    data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
                }
                return data;
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
                initDefaultData(easyUiXml);
            }

            @Override
            public void afterCreate() {
                dealInnerLogic();
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


    private void initDefaultData(EasyUiXml easyUiXml) {
        Location location = Constant.location;
        UiXml dcrq = easyUiXml.getUiXml("DCRQ");
        UiXml dcr = easyUiXml.getUiXml("DCR");
        if (DataStatus.isAdd(feature)) {
            dcrq.setValue(DateUtil.getCurrentCalendar());
            dcr.setValue(Constant.getUser().getName());
            //设置经度纬度海拔
            if (location != null) {
                UiXml alt = easyUiXml.getUiXml("HAIBA");
                if (alt != null) alt.setValue(location.getAltitude());
            }
        }
    }

    /**
     * 处理内部逻辑
     */
    private void dealInnerLogic() {
        //中文名
        ComplexTextView name = propertyView.getViewByKey("ZYSZ");

        //主要树种就是要保护的
        ComplexTextView ke = propertyView.getViewByKey("KE");
        ComplexTextView shu = propertyView.getViewByKey("SHU");
        ComplexTextView zhong = propertyView.getViewByKey("ZHONG");

        WidgetUtil.setViewTextChangeLister(name, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String text) {
                String[] info = getBiologyInfo(text);
                if (info == null || info.length < 4) return;
                zhong.setText(info[0]);
                ke.setText(info[1]);
                shu.setText(info[2]);
            }
        });


        ComplexTextView city = propertyView.getViewByKey("XIAN");
        ComplexTextView county = propertyView.getViewByKey("XIANG");
        ComplexTextView village = propertyView.getViewByKey("CUN");
        WidgetUtil.setViewTextChangeLister(city, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String name) {
                county.setText("");
            }
        });

        WidgetUtil.setViewTextChangeLister(county, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String name) {
                village.setText("");
            }
        });
    }

    private String[] getBiologyInfo(String name) {
        String[] biology = getResources().getStringArray(R.array.array_biology_string);
        if (biology == null) return null;
        String[] info;
        for (String flag : biology) {
            if (flag == null) continue;
            if (flag.startsWith(name)) {
                info = flag.split("\\|");
                return info;
            }
        }
        return null;
    }

}
