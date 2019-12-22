package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.util.IntentUtil;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.ConserveLog;
import com.zy.foxui.util.ObjectUtil;

import java.util.Map;


/**
 * 添加施工日志
 */
public class ConserveLogAty extends BaseMvpCompatAty implements View.OnClickListener {

    private ConserveLog log;
    private EasyUiXml easyUiXml;
    private PropertyView propertyView;
    private Button btnExit, btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log = IntentUtil.getTData(_context, "log");
        initTitle(R.drawable.ic_back, "施工日志", Gravity.CENTER);
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
        easyUiXml = IoXml.readXmlFromAssets(_context, EasyUiXml.class, "patrolv1/XML_CONSERVE_MOUDLE_LOG.xml");
        Map map = ObjectUtil.Common.convert(log, Map.class);
        easyUiXml = Resolution.convert2EasyUiXml(easyUiXml, map);
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
