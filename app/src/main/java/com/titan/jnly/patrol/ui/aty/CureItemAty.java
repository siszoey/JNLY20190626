package com.titan.jnly.patrol.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.arcruntime.util.CustomUtil;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.data.local.sqlite.utils.UUIDTool;
import com.lib.bandaid.data.remote.entity.TTFileResult;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.data.remote.utils.OkHttp3Util;
import com.lib.bandaid.message.FuncManager;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.service.imp.ServiceLocation;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.DateUtil;
import com.lib.bandaid.util.DecimalFormats;
import com.lib.bandaid.util.ImgUtil;
import com.lib.bandaid.util.MapUtil;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.SimpleList;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.widget.collect.image.CollectImgAty;
import com.lib.bandaid.widget.collect.image.CollectImgBean;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui.EventImageView;
import com.lib.bandaid.widget.easyui.ui_v1.ComplexTextView;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.invest.api.ApiFileSync;
import com.titan.jnly.patrol.api.IPatrolCure;
import com.titan.jnly.patrol.bean.CureModel;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.bean.District;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class CureItemAty extends BaseMvpCompatAty implements View.OnClickListener {

    private Map treeData;
    private EasyUiXml easyUiXml;
    private PropertyView propertyView;
    private Button btnExit, btnSubmit;
    private String userId = Constant.getUserInfo().getId();
    private String uuid;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "养护信息", Gravity.CENTER);
        if (getIntent() != null) {
            treeData = OSerial.getData(getIntent(), Map.class);
            isEdit = treeData.get("IS_EDIT") == null ? false : true;
            if (isEdit) uuid = (String) treeData.get("Id");
            else uuid = UUIDTool.get36UUID();
        }
        setContentView(R.layout.patrol_ui_aty_cure_add_layout);
    }

    @Override
    protected void initialize() {
        propertyView = $(R.id.propertyView);
        btnExit = $(R.id.btnExit);
        btnSubmit = $(R.id.btnSubmit);
    }

    @Override
    protected void registerEvent() {
        btnExit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        propertyView.setImgAdapter(new PropertyView.ImgAdapter() {
            @Override
            public String adapter(Object val) {
                UiXml uiXml = easyUiXml.getUiXmlByAlias("养护照片");
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
                if (v instanceof ImageView) {
                    LinkedHashMap waterMark = new LinkedHashMap();
                    String lat = null, lon = null;
                    if (ServiceLocation._location != null) {
                        lat = CustomUtil._10To60_len2(ServiceLocation._location.getLatitude() + "");
                        lon = CustomUtil._10To60_len2(ServiceLocation._location.getLongitude() + "");
                    }
                    //waterMark.put("序号", sequence);
                    waterMark.put("纬度", ObjectUtil.removeNull(lat));
                    waterMark.put("经度", ObjectUtil.removeNull(lon));
                    waterMark.put("时间", DateUtil.dateTimeToStr(new Date()));

                    UiXml uiXml = easyUiXml.getUiXmlByAlias("养护照片");
                    String json = ((EventImageView) uiXml.getView()).getJson();
                    ArrayList<CollectImgBean> beans = CollectImgBean.convertFromJson(json);
                    String imgFPath = Constant.createUserFilePath(uuid);
                    imgFPath = FileUtil.usePathSafe(imgFPath);
                    CollectImgAty.start(_activity, 1000, beans, false, imgFPath, true, true, waterMark);
                }
            }
        });
    }

    @Override
    protected void initClass() {
        easyUiXml = IoXml.readXmlFromAssets(_context, EasyUiXml.class, "patrol/XML_CURE_MOUDLE.xml");
        propertyView.setListener(new ILifeCycle() {
            @Override
            public void beforeCreate() {
                beforeFrom();
            }

            @Override
            public void afterCreate() {

            }
        }).resolutionData(easyUiXml);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExit) {
            onBackPressed();
        }
        if (v.getId() == R.id.btnSubmit) {
            boolean verify = propertyView.verifyForm();
            if (verify) syncFile(propertyView.getForm());
        }
    }

    private void beforeFrom() {
        easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, treeData);
        UiXml xian = easyUiXml.getUiXmlByAlias("县(市、区)");
        UiXml xiang = easyUiXml.getUiXmlByAlias("乡镇(街道)");
        UiXml cun = easyUiXml.getUiXmlByAlias("村(居委会)");
        initArea(xian);
        initArea(xiang);
        initArea(cun);
        //------------------------------------------------------------------------------------------
        //养护时间
        UiXml date = easyUiXml.getUiXmlByAlias("养护时间");
        //养护单位
        UiXml dep = easyUiXml.getUiXmlByAlias("养护单位");
        //养护人
        UiXml user = easyUiXml.getUiXmlByAlias("养护员");
        date.setValue(DateUtil.getCurrentDateTime());
        user.setValue(Constant.getUserInfo().getName());
        //dep.setValue(Constant.getUserInfo().getUserJurs());
        //------------------------------------------------------------------------------------------
        UiXml lon = easyUiXml.getUiXml("LON");
        UiXml lat = easyUiXml.getUiXml("LAT");
        String _60Lon = CustomUtil._10To60_len2(lon.getValue() + "");
        if (lon != null) lon.setValue(_60Lon);
        String _60Lat = CustomUtil._10To60_len2(lat.getValue() + "");
        if (lat != null) lat.setValue(_60Lat);
    }

    private void initArea(UiXml uiXml) {
        Map fields = new SimpleMap<>().push("areaCode", "code").push("areaName", "value");
        String where = " where f_code = '" + uiXml.getValue() + "'";
        List<District> list = DbManager.create(Config.APP_DIC_DB_PATH).getListTByWhere(District.class, where);
        List<ItemXml> data = ObjectUtil.createListTFromList(list, ItemXml.class, fields);
        uiXml.setItemXml(data);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1000) {
            ArrayList<CollectImgBean> beans = (ArrayList<CollectImgBean>) data.getSerializableExtra(CollectImgAty.IMG);
            CollectImgBean.convertToWorkDir(Constant.createUserFilePath(uuid), beans);
            String json = ObjectUtil.convert(beans, String.class);
            UiXml uiXml = easyUiXml.getUiXmlByAlias("养护照片");
            EventImageView view = (EventImageView) uiXml.getView();
            if (view != null) view.setJson(json);
            ImgUtil.simpleLoadImg(view, beans.size() == 0 ? "" : beans.get(0).getUri());
        }
    }


    /**
     * 上传图片
     */
    void syncFile(Map map) {
        String fileJson = (String) map.get("MaintainImgs");
        List<CollectImgBean> beans = ObjectUtil.convert(fileJson, CollectImgBean.class);
        if (ObjectUtil.isEmpty(beans)) {
            showLongToast("请上传图片！");
            return;
        }

        List<TTFileResult> results = new ArrayList<>();
        for (CollectImgBean bean : beans) {
            if (bean.isLocal()) {
                netEasyReq.request(ApiFileSync.class, new NetWorkListen<TTFileResult>() {
                    @Override
                    public void onSuccess(TTFileResult data) {
                        results.add(data);
                        if (results.size() == beans.size()) {
                            syncData(results);
                        }
                    }
                }).httpFileSync(bean.getMultipartBody());
            }else {
                TTFileResult data = ObjectUtil.convert(bean.getTag(), TTFileResult.class);
                results.add(data);
                if (results.size() == beans.size()) {
                    syncData(results);
                }
            }
        }
    }

    void syncData(List<TTFileResult> fileResult) {
        CureModel model = new CureModel();
        model.setUserId(userId);
        Map map = easyUiXml.getFormMap();
        map.put("Id", uuid);
        ComplexTextView lonView = propertyView.getViewByKey("LON");
        ComplexTextView latView = propertyView.getViewByKey("LAT");
        map.put("LON", CustomUtil._60To10(lonView.getText()));
        map.put("LAT", CustomUtil._60To10(latView.getText()));

        model.setMaintainRecord(map);
        model.setMaintainImgs(fileResult);

        List<CureModel> param = new SimpleList<>().push(model);
        String json = ObjectUtil.convert(param, String.class);
        System.out.println(json);

        netEasyReq.request(IPatrolCure.class, new NetWorkListen<TTResult>() {
            @Override
            public void onSuccess(TTResult data) {
                showLongToast("提交成功！");
                finish();
                if(isEdit) {
                    FuncManager.getInstance().invokeFunc(CureListAty.FUNC_CURE_EDIT, model);
                }else {
                    FuncManager.getInstance().invokeFunc(CureListAty.FUNC_CURE_ADD, model);
                }
            }
        }).httpPostCureItem(param);
    }
}
