package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.R;

public class PatrolMsgAty extends BaseMvpCompatAty implements View.OnClickListener {

    private EasyUiXml easyUiXml;
    private PropertyView propertyView;
    private Button btnExit, btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "巡查消息", Gravity.CENTER);
        setContentView(R.layout.patrolv1_ui_com_form_layout);
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
    }

    @Override
    protected void initClass() {
        easyUiXml = IoXml.readXmlFromAssets(_context, EasyUiXml.class, "patrolv1/XML_PATROL_MOUDLE_MSG.xml");
        propertyView.setListener(new ILifeCycle() {
            @Override
            public void beforeCreate() {
            }

            @Override
            public void afterCreate() {

            }
        }).resolutionData(easyUiXml);
    }

    @Override
    public void onClick(View v) {

    }
}
