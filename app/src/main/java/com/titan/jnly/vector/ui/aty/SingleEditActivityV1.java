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
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.util.TransformUtil;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.service.imp.ServiceLocation;
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
import com.titan.jnly.vector.bean.Species;
import com.titan.jnly.vector.enums.DataStatus;
import com.titan.jnly.vector.tool.SketchEditorTools;
import com.titan.jnly.vector.util.DbEasyUtil;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 单株古树名木调查属性编辑
 */
public class SingleEditActivityV1 extends BaseAppCompatActivity implements View.OnClickListener {

    private String uuid;

    private Button btnExit, btnSubmit;
    private FrameLayout flRoot;

    public static Feature lastFeature;

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

    //接收消息
    @Subscribe
    public void onEventMainThread(Object object) {

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
                    LinkedHashMap waterMark = new LinkedHashMap();
                    String lat = TransformUtil._10To60_len2(ServiceLocation._location.getLatitude() + "");
                    String lon = TransformUtil._10To60_len2(ServiceLocation._location.getLongitude() + "");

                    waterMark.put("序号", "001");
                    waterMark.put("纬度", lat);
                    waterMark.put("经度", lon);
                    waterMark.put("时间", DateUtil.dateTimeToStr(new Date()));
                    String imgPath = FileUtil.usePathSafe(Config.APP_PHOTO_PATH.concat(File.separator).concat(uuid));
                    //PhotoActivity.start(_context, false, imgPath, true, true, waterMark);
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

       // feature = data.getData();
      //  uuid = FeatureUtil.getAsT(feature, "UUID");
      //  feaTable = ((LayerNode) data.getTag()).tryGetFeaTable();

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
        //新增预处理
        if (DataStatus.isAdd(feature)) {
            Location location = Constant.location;
            UiXml dcrq = easyUiXml.getUiXml("DCRQ");
            UiXml dcr = easyUiXml.getUiXml("DCR");
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
            if (lastFeature == null) return;
            Map lastAttr = lastFeature.getAttributes();


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
            List<District> list = DbManager.create(Config.APP_DIC_DB_PATH).getListTByWhere(District.class, where);
            data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
        }
        if (flag.equals("XIANG")) {
            UiXml city = easyUiXml.getUiXml("XIAN");
            Object val = city.getValue();
            String where = " where length(f_code) = 9 and substr(f_code,1,6) = '" + val + "'";
            List<District> list = DbManager.create(Config.APP_DIC_DB_PATH).getListTByWhere(District.class, where);
            data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
        }
        if (flag.equals("CUN")) {
            UiXml county = easyUiXml.getUiXml("XIANG");
            Object val = county.getValue();
            String where = " where length(f_code) = 12 and substr(f_code,1,9) = '" + val + "'";
            List<District> list = DbManager.create(Config.APP_DIC_DB_PATH).getListTByWhere(District.class, where);
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
        ComplexTextView plantPlace = propertyView.getViewByAlias("生长区域");

        WidgetUtil.setViewTextChangeLister(cycle, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String cycleSize) {
                String name = zhName.getText();
                String place = plantPlace.getText();
                if (ObjectUtil.isEmpty(place)) return;
                if (ObjectUtil.isEmpty(name)) return;
                if (!NumberUtil.can2Double(cycleSize)) return;
                int age = DbEasyUtil.computeTreeAgeByCycle(name, Double.parseDouble(cycleSize), place);
                modeAge.setText(age + "");
            }
        });

        WidgetUtil.setViewTextChangeLister(zhName, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String name) {
                String cycleSize = cycle.getText();
                String place = plantPlace.getText();
                if (ObjectUtil.isEmpty(name)) return;
                if (!NumberUtil.can2Double(cycleSize)) return;
                if (ObjectUtil.isEmpty(place)) return;
                int age = DbEasyUtil.computeTreeAgeByCycle(name, Double.parseDouble(cycleSize), place);
                modeAge.setText(age + "");
            }
        });

        WidgetUtil.setViewTextChangeLister(plantPlace, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String place) {
                String name = zhName.getText();
                String cycleSize = cycle.getText();
                if (ObjectUtil.isEmpty(name)) return;
                if (ObjectUtil.isEmpty(place)) return;
                if (!NumberUtil.can2Double(cycleSize)) return;
                int age = DbEasyUtil.computeTreeAgeByCycle(name, Double.parseDouble(cycleSize), place);
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

    //接收消息
    @Subscribe
    public void onEventMainThread(ServiceLocation location) {
        String lat = TransformUtil._10To60_len2(ServiceLocation._location.getLatitude() + "");
        String lon = TransformUtil._10To60_len2(ServiceLocation._location.getLongitude() + "");
        PhotoActivity.updateMaker().put("纬度", lat);
        PhotoActivity.updateMaker().put("经度", lon);
    }
}
