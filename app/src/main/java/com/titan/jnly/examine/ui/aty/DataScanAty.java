package com.titan.jnly.examine.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.arcruntime.util.CustomUtil;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.util.DecimalFormats;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.bean.District;

import java.util.List;
import java.util.Map;

public class DataScanAty extends BaseAppCompatActivity {

    private Map property;
    private PropertyView propertyView;
    private EasyUiXml easyUiXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "古树信息", Gravity.CENTER);
        if (getIntent() != null) {
            OSerial<Map> serial = (OSerial) getIntent().getSerializableExtra("data");
            property = serial.getData(Map.class);
        }
        propertyView = new PropertyView(_context);
        setContentView(propertyView);
    }


    @Override
    protected void initialize() {
        easyUiXml = Constant.getEasyUiXmlByName(_context, "古树名木单株调查");
        easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, property);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        propertyView.setListener(new ILifeCycle() {
            @Override
            public void beforeCreate() {
                initDefaultData(easyUiXml);
            }

            @Override
            public void afterCreate() {
            }
        }).resolutionData(easyUiXml);
    }

    private void initDefaultData(EasyUiXml easyUiXml) {
        UiXml lon = easyUiXml.getUiXml("LON");
        UiXml lat = easyUiXml.getUiXml("LAT");
        UiXml alt = easyUiXml.getUiXml("HAIBA");
        String _60Lon = CustomUtil._10To60_len2(lon.getValue() + "");
        if (lon != null) lon.setValue(_60Lon);
        String _60Lat = CustomUtil._10To60_len2(lat.getValue() + "");
        if (lat != null) lat.setValue(_60Lat);
        String alts = DecimalFormats.getFormat("#.00").format(alt.getValue());
        if (alt != null) alt.setValue(alts);
        initArea(easyUiXml.getUiXml("XIAN"));
        initArea(easyUiXml.getUiXml("XIANG"));
        initArea(easyUiXml.getUiXml("CUN"));

        //树种中文名称赋值
       // List<ItemXml> items = ObjectUtil.createListTFromList(Constant.getSpecies(), ItemXml.class, new SimpleMap<>().push("code", "code").push("species", "value").toMap());
      //  UiXml item = easyUiXml.getUiXml("SZZWM");
      //  item.setItemXml(items);
    }

    private void initArea(UiXml uiXml) {
        String flag = uiXml.getCode();
        List<ItemXml> data = null;
        Map fields = new SimpleMap<>().push("areaCode", "code").push("areaName", "value");
        if (flag.equals("XIAN")) {
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
}
