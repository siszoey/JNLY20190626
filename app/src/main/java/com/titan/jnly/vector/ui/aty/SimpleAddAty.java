package com.titan.jnly.vector.ui.aty;

import android.content.Intent;
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
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.util.TransformUtil;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.data.local.sqlite.utils.UUIDTool;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.service.imp.ServiceLocation;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.DecimalFormats;
import com.lib.bandaid.utils.ImgUtil;
import com.lib.bandaid.utils.MapUtil;
import com.lib.bandaid.utils.NumberUtil;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.SimpleMap;
import com.lib.bandaid.utils.TimePickerDialogUtil;
import com.lib.bandaid.utils.ToastUtil;
import com.lib.bandaid.widget.collect.image.CollectImgAty;
import com.lib.bandaid.widget.collect.image.CollectImgBean;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui.EventImageView;
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
import com.titan.jnly.vector.util.PropertyUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 单株古树名木调查属性编辑
 */
public class SimpleAddAty extends BaseAppCompatActivity implements View.OnClickListener {

    private String uuid;

    private Button btnExit, btnSubmit;
    private FrameLayout flRoot;


    private Map<String, Object> property;
    public Feature lastFeature;
    private FeatureTable feaTable;


    private PropertyView propertyView;
    private SketchEditorTools tools;
    private EasyUiXml easyUiXml;
    private String imgFPath;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveData(Object[] objects) {
        this.feaTable = (FeatureTable) objects[0];
        this.lastFeature = (Feature) objects[1];
    }

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

                UiXml uiXml = easyUiXml.getUiXml("GSZP");
                EventImageView view = (EventImageView) uiXml.getView();
                if (view != null) view.setJson((String) val);

                List<CollectImgBean> beans = CollectImgBean.convertFromJson((String) val);
                if (beans == null || beans.size() == 0) return null;
                return beans.get(0).getUri();
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
                    waterMark.put("序号", sequence);
                    waterMark.put("纬度", lat);
                    waterMark.put("经度", lon);
                    waterMark.put("时间", DateUtil.dateTimeToStr(new Date()));

                    UiXml uiXml = easyUiXml.getUiXml("GSZP");
                    String json = ((EventImageView) uiXml.getView()).getJson();
                    ArrayList<CollectImgBean> beans = CollectImgBean.convertFromJson(json);
                    CollectImgAty.start(_activity, 1000, beans, false, imgFPath, true, true, waterMark);
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

    String sequence;

    @Override
    protected void initClass() {
        uuid = UUIDTool.get32UUID();//新增UUID
        imgFPath = FileUtil.usePathSafe(Config.APP_PHOTO_PATH.concat(File.separator).concat(uuid));
        easyUiXml = Constant.getEasyUiXmlByName(_context, feaTable.getTableName());

        {
            sequence = DbEasyUtil.getWorkSequence();
            if (sequence == null) {
                ToastUtil.showLong(_context, "未能取得调查顺序号！");
                finish();
                return;
            }
            property = DataStatus.createAdd();
            property.put("DCSXH", sequence);
            property.putAll(DataStatus.createEdit());
        }

        if (lastFeature != null) {
            Map lastAttr = lastFeature.getAttributes();
            PropertyUtil.copyUseFulAttr(lastAttr, property);
        }
        easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, property);


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


    //新增预处理
    private void initDefaultData(EasyUiXml easyUiXml) {

        UiXml eleSign = easyUiXml.getUiXml("DZBQH");
        eleSign.setValue(Constant.getUserInfo().getUserJurs().substring(0, 6));

        Location location = ServiceLocation._location;
        UiXml dcrq = easyUiXml.getUiXml("DCRQ");
        UiXml dcr = easyUiXml.getUiXml("DCR");
        dcrq.setValue(DateUtil.getCurrentCalendar());
        dcr.setValue(Constant.getUser().getName());
        //设置经度纬度海拔
        if (location != null) {
            UiXml lon = easyUiXml.getUiXml("LON");
            UiXml lat = easyUiXml.getUiXml("LAT");
            UiXml alt = easyUiXml.getUiXml("HAIBA");
            String _60Lon = TransformUtil._10To60_len2(location.getLongitude() + "");
            //if (lon != null) lon.setValue(location.getLongitude());
            if (lon != null) lon.setValue(_60Lon);
            String _60Lat = TransformUtil._10To60_len2(location.getLatitude() + "");
            //if (lat != null) lat.setValue(location.getLatitude());
            if (lat != null) lat.setValue(_60Lat);
            String alts = DecimalFormats.getFormat("#0.00").format(location.getAltitude());
            if (alt != null) alt.setValue(alts);
        }
        initArea(easyUiXml.getUiXml("XIAN"));
        initArea(easyUiXml.getUiXml("XIANG"));
        initArea(easyUiXml.getUiXml("CUN"));

        //树种中文名称赋值
        List<ItemXml> items = ObjectUtil.createListTFromList(Constant.getSpecies(), ItemXml.class, new SimpleMap<>().push("code", "code").push("species", "value").toMap());
        UiXml item = easyUiXml.getUiXml("SZZWM");
        item.setItemXml(items);
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
            ComplexTextView lonView = propertyView.getViewByKey("LON");
            ComplexTextView latView = propertyView.getViewByKey("LAT");
            map.put("LON", TransformUtil._60To10(lonView.getText()));
            map.put("LAT", TransformUtil._60To10(latView.getText()));
            property.putAll(map);
            Double lon = (Double) property.get("LON");
            Double lat = (Double) property.get("LAT");
            Point _84Point = new Point(lon, lat, SpatialReference.create(4326));
            tools.addFeature(feaTable, _84Point, property, new SketchEditorTools.ICallBack() {
                @Override
                public void ok() {
                    finish();
                }
            });
        }
    }

    /**
     * 处理控件内部的逻辑
     */
    private void dealInnerLogic() {
        //---------------------------------------科属种关联-----------------------------------------
        //中文名
        ComplexTextView name = propertyView.getViewByAlias("树种中文名");
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

        //----------------------------------------树龄计算------------------------------------------
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
                if (age == -1) {
                    ToastUtil.showLong(_context, "未能找到该树种的计算模型，请联系管理员！");
                }
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
                if (age == -1) {
                    ToastUtil.showLong(_context, "未能找到该树种的计算模型，请联系管理员！");
                }
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
                if (age == -1) {
                    ToastUtil.showLong(_context, "未能找到该树种的计算模型，请联系管理员！");
                }
                modeAge.setText(age + "");
            }
        });

        //----------------------------------------区划关联------------------------------------------
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

        //----------------------------------------区划关联------------------------------------------
        ComplexTextView ew = propertyView.getViewByKey("DXGF");
        ComplexTextView sn = propertyView.getViewByKey("NBGF");
        ComplexTextView wsen = propertyView.getViewByKey("PJGF");
        WidgetUtil.setViewTextChangeLister(ew, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String text) {
                String snText = sn.getText();
                if (!NumberUtil.can2Double(text) || !NumberUtil.can2Double(snText)) return;
                double avg = (NumberUtil.try2Double(text) + NumberUtil.try2Double(snText)) / 2;
                wsen.setText(avg + "");
            }
        });
        WidgetUtil.setViewTextChangeLister(sn, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String text) {
                String ewText = ew.getText();
                if (!NumberUtil.can2Double(text) || !NumberUtil.can2Double(ewText)) return;
                double avg = (NumberUtil.try2Double(text) + NumberUtil.try2Double(ewText)) / 2;
                wsen.setText(avg + "");
            }
        });

        //--------------------------------------坡度等级关联----------------------------------------
        ComplexTextView slope = propertyView.getViewByKey("PODU");
        UiXml slopeLevel = propertyView.getUiXmlByKey("PDDJ");
        WidgetUtil.setViewTextChangeLister(slope, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String text) {
                if (!NumberUtil.can2Double(text)) {
                    slopeLevel.setLabel("");
                    return;
                }
                double data = NumberUtil.try2Double(text);
                if (data < 5) slopeLevel.setLabel("Ⅰ/平坡");
                if (data >= 5 && data <= 14) slopeLevel.setLabel("Ⅱ/缓坡");
                if (data >= 15 && data <= 24) slopeLevel.setLabel("Ⅲ/斜坡");
                if (data >= 25 && data <= 34) slopeLevel.setLabel("Ⅳ/陡坡");
                if (data >= 35 && data <= 44) slopeLevel.setLabel("Ⅵ/急坡");
                if (data > 45) slopeLevel.setLabel("Ⅶ/险坡");
            }
        });

        //------------------------------------------------------------------------------------------
        //经纬度特殊处理
        ComplexTextView lonView = propertyView.getViewByKey("LON");
        lonView.setListenChange(new ComplexTextView.IListenChange() {
            @Override
            public void textChange(int id, String text) {
                if (!text.contains("°")) {
                    text = text.replace(" ", "°");
                }
                if (text.contains("°") && !text.contains("′")) {
                    text = text.replace(" ", "′");
                }
                if (text.contains("°") && text.contains("′") && !text.contains("″")) {
                    text = text.replace(" ", "″");
                }
                lonView.setText(text, false);
                lonView.keepCursorLast();

            }
        });

        //经纬度特殊处理
        ComplexTextView latView = propertyView.getViewByKey("LAT");
        latView.setListenChange(new ComplexTextView.IListenChange() {
            @Override
            public void textChange(int id, String text) {
                if (!text.contains("°")) {
                    text = text.replace(" ", "°");
                }
                if (text.contains("°") && !text.contains("′")) {
                    text = text.replace(" ", "′");
                }
                if (text.contains("°") && text.contains("′") && !text.contains("″")) {
                    text = text.replace(" ", "″");
                }
                latView.setText(text, false);
                latView.keepCursorLast();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1000) {
            ArrayList<CollectImgBean> beans = (ArrayList<CollectImgBean>) data.getSerializableExtra(CollectImgAty.IMG);
            //将选择的图片转移到工作目录
            CollectImgBean.convertToWorkDir(imgFPath, beans);
            //转成json存储
            String json = MapUtil.entity2Json(beans);
            UiXml uiXml = easyUiXml.getUiXml("GSZP");
            EventImageView view = (EventImageView) uiXml.getView();
            if (view != null) view.setJson(json);
            ImgUtil.simpleLoadImg(view, beans.size() == 0 ? "" : beans.get(0).getUri());
        }
    }
}