package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.titan.jnly.R;
import com.zy.foxui.easyui.convert.Resolution;
import com.zy.foxui.easyui.ui.UiViewEasy;
import com.zy.foxui.easyui.xml.XmlUiEasy;

import java.util.HashMap;
import java.util.Map;


public class FieldInquireAty extends BaseMvpCompatAty implements View.OnClickListener {

    private UiViewEasy uiViewEasy;
    private Button btnExit, btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "巡查日志信息", Gravity.CENTER);
        setContentView(R.layout.patrol_ui_aty_info_layout);
    }

    @Override
    public void initialize() {
        uiViewEasy = $(R.id.uiViewEasy);
        btnExit = $(R.id.btnExit);
        btnSubmit = $(R.id.btnSubmit);
    }

    @Override
    public void registerEvent() {
        btnExit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void initClass() {
        XmlUiEasy xmlUiEasy = IoXml.readXmlFromAssets(_context, XmlUiEasy.class, "patrol/MOUDLE_SAMPLE_PLOT.xml");

        Map set = new HashMap();
        set.put("ydbh", "333");
        set.put("ydzs", 100);
        set.put("bz", "dusiadsahuhdusahdusahdusa你好");
        Resolution.combineEasyUiXml(xmlUiEasy, set);
        uiViewEasy.setData(xmlUiEasy).resolution();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExit) {

        }
        if (v.getId() == R.id.btnSubmit) {
            uiViewEasy.checkAllForm();
            Map data = uiViewEasy.getForm();
            System.out.println(data);
        }
    }
}
