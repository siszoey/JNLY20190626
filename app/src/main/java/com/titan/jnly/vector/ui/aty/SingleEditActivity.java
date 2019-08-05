package com.titan.jnly.vector.ui.aty;

import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.NumberUtil;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.SimpleMap;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui.PropertyEditView;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.TimePickerDialogUtil;
import com.lib.bandaid.widget.easyui.ui_v1.ComplexTextView;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.utils.WidgetUtil;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.lib.bandaid.widget.text.SimpleTextWatch;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.login.bean.User;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.bean.District;
import com.titan.jnly.vector.bean.Species;
import com.titan.jnly.vector.enums.DataStatus;
import com.titan.jnly.vector.tool.SketchEditorTools;
import com.titan.jnly.vector.util.TreeModeUtil;

import java.util.List;
import java.util.Map;

/**
 * 单株古树名木调查属性编辑
 */
public class SingleEditActivity extends BaseAppCompatActivity implements View.OnClickListener {

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
            public void listAdapter(UiXml uiXml) {
                initArea(uiXml);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tools = new SketchEditorTools(ArcMap.arcMap);
    }

    @Override
    protected void initClass() {
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

    private void initDefaultData(EasyUiXml easyUiXml) {
        Location location = Constant.location;
        UiXml dcrq = easyUiXml.getUiXml("DCRQ");
        UiXml dcr = easyUiXml.getUiXml("DCR");
        if (DataStatus.isAdd(feature)) {
            dcrq.setValue(DateUtil.getCurrentCalendar());
            dcr.setValue(Constant.getUser().getName());
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
        //替换区划代码
        else {
            initArea(easyUiXml.getUiXml("XIAN"));
            initArea(easyUiXml.getUiXml("XIANG"));
            initArea(easyUiXml.getUiXml("CUN"));
        }
        //树种中文名称赋值
        List<ItemXml> items = ObjectUtil.createListTFromList(Constant.getSpecies(), ItemXml.class, new SimpleMap<>().push("code", "code").push("species", "value").toMap());
        UiXml item = easyUiXml.getUiXml("SZZWM");
        item.setItemXml(items);
        //经纬度特殊处理
        // UiXml LONXml = easyUiXml.getUiXml("LON");
        // UiXml LATXml = easyUiXml.getUiXml("LAT");

        /*LONXml.setTextAfter(new SimpleTextWatch.IAfter() {

            @Override
            public void after(String s) {
                System.out.println(s);
            }
        });*/


        /*LONXml.setOnChange(new SimpleTextWatch.IOnChange() {

            @Override
            public void onChange(CharSequence s, int start, int before, int count) {
                //((ComplexTextView) LONXml.getView()).setText(s + "|");
            }
        });


        LATXml.setTextBefore(new SimpleTextWatch.IBefore() {
            @Override
            public void before(CharSequence s, int start, int count, int after) {
                System.out.println(s);
            }
        });*/

    }

    private void initArea(UiXml uiXml) {
        String flag = uiXml.getCode();
        List<ItemXml> data = null;
        Map fields = new SimpleMap<>().push("areaCode", "code").push("areaName", "value");
        if (flag.equals("XIAN")) {
            String where = " where length(f_code) = 6";
            List<District> list = DbManager.createDefault().getListTByWhere(District.class, where);
            data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
        }
        if (flag.equals("XIANG")) {
            UiXml city = easyUiXml.getUiXml("XIAN");
            Object val = city.getValue();
            String where = " where length(f_code) = 9 and substr(f_code,1,6) = '" + val + "'";
            List<District> list = DbManager.createDefault().getListTByWhere(District.class, where);
            data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
        }
        if (flag.equals("CUN")) {
            UiXml county = easyUiXml.getUiXml("XIANG");
            Object val = county.getValue();
            String where = " where length(f_code) = 12 and substr(f_code,1,9) = '" + val + "'";
            List<District> list = DbManager.createDefault().getListTByWhere(District.class, where);
            data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
        }
        uiXml.setItemXml(data);
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
            map.putAll(DataStatus.createEdit());
            feature.getAttributes().putAll(map);
            tools.updateFeature(feaTable, feature, new SketchEditorTools.ICallBack() {
                @Override
                public void ok() {
                    finish();
                }
            });
        }
    }

    /**
     * 处理内部逻辑
     */
    private void dealInnerLogic() {
        //中文名
        ComplexTextView name = propertyView.getViewByAlias("树种中文名");
        //俗名
        ComplexTextView petName = propertyView.getViewByAlias("树种俗名");
        //拉丁名称
        ComplexTextView latinName = propertyView.getViewByAlias("树种拉丁名");
        //科
        ComplexTextView ke = propertyView.getViewByAlias("树种科");
        //属
        ComplexTextView shu = propertyView.getViewByAlias("树种_属");
        WidgetUtil.setViewTextChangeLister(name, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String text) {
                Species species = Constant.getSpecies(text);
                if (species == null) return;
                ke.setText(species.getFamily());
                shu.setText(species.getGenus());
                latinName.setText(species.getIatin());
            }
        });

        ComplexTextView modeAge = propertyView.getViewByAlias("回归模型树龄");
        ComplexTextView cycle = propertyView.getViewByKey("XJ");
        ComplexTextView zhName = propertyView.getViewByAlias("树种中文名");

        WidgetUtil.setViewTextChangeLister(cycle, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String cycleSize) {
                String name = zhName.getText();
                if (ObjectUtil.isEmpty(name)) return;
                if (!NumberUtil.can2Double(cycleSize)) return;
                int age = TreeModeUtil.computeTreeAgeByCycle(name, Double.parseDouble(cycleSize));
                modeAge.setText(age + "");
            }
        });

        WidgetUtil.setViewTextChangeLister(zhName, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String name) {
                if (ObjectUtil.isEmpty(name)) return;
                String cycleSize = cycle.getText();
                if (!NumberUtil.can2Double(cycleSize)) return;
                int age = TreeModeUtil.computeTreeAgeByCycle(name, Double.parseDouble(cycleSize));
                modeAge.setText(age + "");
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

    @Override
    protected void onDestroy() {
        if (easyUiXml != null) easyUiXml.release();
        super.onDestroy();
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
