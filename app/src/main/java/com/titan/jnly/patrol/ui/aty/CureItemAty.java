package com.titan.jnly.patrol.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.R;

public class CureItemAty extends BaseMvpCompatAty {

    private EasyUiXml easyUiXml;
    private PropertyView propertyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "添加养护信息", Gravity.CENTER);
        setContentView(R.layout.patrol_ui_aty_cure_add_layout);
    }

    @Override
    protected void initialize() {
        propertyView = $(R.id.propertyView);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        easyUiXml = IoXml.readXmlFromAssets(_context, EasyUiXml.class, "patrol/XML_CURE_MOUDLE.xml");
        propertyView.setListener(new ILifeCycle() {
            @Override
            public void beforeCreate() {

            }

            @Override
            public void afterCreate() {

            }
        }).resolutionData(easyUiXml);
    }
}
