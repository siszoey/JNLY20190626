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
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.util.CustomUtil;
import com.lib.bandaid.arcruntime.util.FeatureUtil;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.service.imp.ServiceLocation;
import com.lib.bandaid.util.DateUtil;
import com.lib.bandaid.util.DecimalFormats;
import com.lib.bandaid.util.ImgUtil;
import com.lib.bandaid.util.JsonUtil;
import com.lib.bandaid.util.MapUtil;
import com.lib.bandaid.util.NumberUtil;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.util.StringUtil;
import com.lib.bandaid.util.ToastUtil;
import com.lib.bandaid.widget.collect.image.CollectImgAty;
import com.lib.bandaid.widget.collect.image.CollectImgBean;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.TimePickerDialogUtil;
import com.lib.bandaid.widget.easyui.ui.EventImageView;
import com.lib.bandaid.widget.easyui.ui_v1.ComplexTextView;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.utils.WidgetUtil;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.lib.bandaid.widget.easyui.xml.VerifyXml;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.bean.District;
import com.titan.jnly.vector.bean.Species;
import com.titan.jnly.vector.enums.DataStatus;
import com.titan.jnly.vector.tool.SketchEditorTools;
import com.titan.jnly.vector.util.PropertyUtil;
import com.titan.jnly.vector.util.DbEasyUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 单株古树名木调查属性编辑
 */
public class SingleEditActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private String uuid;

    private Button btnExit, btnSubmit;
    private FrameLayout flRoot;

    public static Feature data;
    public static Feature lastFeature;

    private PropertyView propertyView;
    private SketchEditorTools tools;
    private FeatureTable feaTable;
    private Feature feature;
    private EasyUiXml easyUiXml;

    private String imgFPath;
    private String _6ele;

    private VerifyXml remarkVerify;

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

        remarkVerify = new VerifyXml();
        remarkVerify.setCanNull(false);
        remarkVerify.setMsg("树种为名木或其他时必写填写30以上");
        remarkVerify.setRegex("^[\\S]{30,}$");

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

                    String lat = "";
                    String lon = "";
                    if (ServiceLocation._location != null) {
                        lat = CustomUtil._10To60_len2(ServiceLocation._location.getLatitude() + "");
                        lon = CustomUtil._10To60_len2(ServiceLocation._location.getLongitude() + "");
                    }

                    waterMark.put("序号", sequence);
                    waterMark.put("纬度", lat);
                    waterMark.put("经度", lon);
                    waterMark.put("时间", DateUtil.dateTimeToStr(new Date()));

                    UiXml uiXml = easyUiXml.getUiXml("GSZP");
                    String json = ((EventImageView) uiXml.getView()).getJson();
                    ArrayList<CollectImgBean> beans = CollectImgBean.convertFromJson(json);

                    imgFPath = FileUtil.usePathSafe(imgFPath);
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

        feature = data;
        feaTable = feature.getFeatureTable();

        uuid = FeatureUtil.getAsT(feature, "UUID");
        sequence = FeatureUtil.getAsT(feature, "DCSXH");
        imgFPath = Constant.createUserFilePath(uuid);

        //获取布局的模板
        easyUiXml = Constant.getEasyUiXmlByName(_context, feaTable.getTableName());
        Map<String, Object> property = feature.getAttributes();
        if (DataStatus.isAdd(feature)) {
            if (lastFeature != null) {
                Map lastAttr = lastFeature.getAttributes();
                PropertyUtil.copyUseFulAttr(lastAttr, property);

                UiXml eleSign = easyUiXml.getUiXml("DZBQH");
                String eleText = FeatureUtil.getAsT(lastFeature, "DZBQH");
                if (eleText != null) {
                    _6ele = eleText.substring(0, 6);
                    eleSign.setValue(_6ele);
                }
            }
            easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, property);
        } else {
            easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, property);
            String eleText = FeatureUtil.getAsT(feature, "DZBQH");
            if (eleText != null) _6ele = eleText.substring(0, 6);
        }
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
            UiXml eleSign = easyUiXml.getUiXml("DZBQH");
            eleSign.setValue(_6ele);

            Location location = ServiceLocation._location;
            UiXml dcrq = easyUiXml.getUiXml("DCRQ");
            UiXml dcr = easyUiXml.getUiXml("DCR");
            dcrq.setValue(DateUtil.getCurrentCalendar());
            dcr.setValue(Constant.getUserInfo().getName());
            //设置经度纬度海拔
            if (location != null) {
                UiXml lon = easyUiXml.getUiXml("LON");
                UiXml lat = easyUiXml.getUiXml("LAT");
                String _60Lon = CustomUtil._10To60_short(location.getLongitude() + "");
                if (lon != null) lon.setValue(_60Lon);
                String _60Lat = CustomUtil._10To60_short(location.getLatitude() + "");
                if (lat != null) lat.setValue(_60Lat);

                UiXml alt = easyUiXml.getUiXml("HAIBA");
                String alts = DecimalFormats.getFormat("#.00").format(location.getAltitude());
                if (alt != null) alt.setValue(alts);
            }
        }
        //替换区划代码
        else {
            UiXml lon = easyUiXml.getUiXml("LON");
            UiXml lat = easyUiXml.getUiXml("LAT");
            UiXml alt = easyUiXml.getUiXml("HAIBA");
            String _60Lon = CustomUtil._10To60_len2(lon.getValue() + "");
            if (lon != null) lon.setValue(_60Lon);
            String _60Lat = CustomUtil._10To60_len2(lat.getValue() + "");
            if (lat != null) lat.setValue(_60Lat);
            String alts = DecimalFormats.getFormat("#.00").format(alt.getValue());
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
            //String where = " where length(f_code) = 6";
            String codes = Constant.getUserInfo().getUserJurs();
            String where = " where f_code in (" + codes + ")";
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
            map.put("LON", CustomUtil._60To10(lonView.getText()));
            map.put("LAT", CustomUtil._60To10(latView.getText()));

            ComplexTextView smallName = propertyView.getViewByKey("XDM");
            ComplexTextView customName = propertyView.getViewByKey("SZSM");
            map.put("XDM", smallName.getText());
            map.put("SZSM", customName.getText());

            map.remove("OBJECTID");
            map.putAll(DataStatus.createEdit());


            feature.getAttributes().putAll(map);
            tools.updateFeature(feaTable, feature, new SketchEditorTools.ICallBack() {
                @Override
                public void ok() {
                    finish();
                    DbEasyUtil.saveWorkSequence(sequence);
                }
            });
        }
    }

    /**
     * 处理控件内部的逻辑
     */
    private void dealInnerLogic() {
        ComplexTextView sqe = propertyView.getViewByKey("DCSXH");
        sqe.setListenChange(new ComplexTextView.IListenChange() {
            @Override
            public void textChange(int id, String text) {
                if (sqe.checkVerify()) sequence = text;
            }
        });

        ComplexTextView selSign = propertyView.getViewByKey("DZBQH");
        selSign.setListenChange(new ComplexTextView.IListenChange() {
            @Override
            public void textChange(int id, String text) {
                if (text.trim().length() < 6) {
                    selSign.setText(_6ele, false);
                } else {
                    text = text.substring(6);
                    text = text.replaceAll("^(0+)", "");
                    text = StringUtil.completeZero(text, 5);
                    if (text.endsWith("00000")) text = "";
                    selSign.setText(_6ele + text, false);
                }
                selSign.keepCursorLast();
            }
        });
        //---------------------------------------科属种关联-----------------------------------------
        //中文名
        ComplexTextView name = propertyView.getViewByAlias("树种中文名");
        //拉丁名称
        ComplexTextView latinName = propertyView.getViewByAlias("树种拉丁名");
        //科
        ComplexTextView ke = propertyView.getViewByAlias("树种科");
        //属
        ComplexTextView shu = propertyView.getViewByAlias("树种_属");

        UiXml makeUi = easyUiXml.getUiXml("REMARK");
        if (name.getText().equals("名木") || name.getText().equals("其他")) {
            makeUi.setVerifyXml(remarkVerify);
        } else {
            makeUi.setVerifyXml(null);
        }

        WidgetUtil.setViewTextChangeLister(name, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String text) {
                Species species = Constant.getSpecies(text);
                if (species == null) return;
                ke.setText(species.getFamily());
                shu.setText(species.getGenus());
                latinName.setText(species.getIatin());

                if (text.equals("名木") || text.equals("其他")) {
                    makeUi.setVerifyXml(remarkVerify);
                } else {
                    makeUi.setVerifyXml(null);
                }
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
        UiXml uiXml = easyUiXml.getUiXml("XIAN");
        ComplexTextView ele = propertyView.getViewByKey("DZBQH");
        WidgetUtil.setViewTextChangeLister(city, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String name) {
                county.setText("");
                {
                    _6ele = (String) uiXml.getViewCode();
                    ele.setText(_6ele);
                }
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
                //wsen.setText(DecimalFormats.getFormat("#0.00").format(avg));
                wsen.setText((int) avg + "");
            }
        });
        WidgetUtil.setViewTextChangeLister(sn, new WidgetUtil.IChangeLister() {
            @Override
            public void changeLister(String text) {
                String ewText = ew.getText();
                if (!NumberUtil.can2Double(text) || !NumberUtil.can2Double(ewText)) return;
                double avg = (NumberUtil.try2Double(text) + NumberUtil.try2Double(ewText)) / 2;
                //wsen.setText(DecimalFormats.getFormat("#0.00").format(avg));
                wsen.setText((int) avg + "");
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
                if (data >= 45) slopeLevel.setLabel("Ⅶ/险坡");
            }
        });


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
        String lat = CustomUtil._10To60_len2(ServiceLocation._location.getLatitude() + "");
        String lon = CustomUtil._10To60_len2(ServiceLocation._location.getLongitude() + "");
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
            String json = JsonUtil.obj2Json(beans);
            UiXml uiXml = easyUiXml.getUiXml("GSZP");
            EventImageView view = (EventImageView) uiXml.getView();
            if (view != null) view.setJson(json);
            ImgUtil.simpleLoadImg(view, beans.size() == 0 ? "" : beans.get(0).getUri());
        }
    }
}
