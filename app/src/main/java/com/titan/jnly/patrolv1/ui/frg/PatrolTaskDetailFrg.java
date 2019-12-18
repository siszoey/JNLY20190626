package com.titan.jnly.patrolv1.ui.frg;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.PatrolTask;
import com.zy.foxui.util.ObjectUtil;

import java.util.Map;

public class PatrolTaskDetailFrg extends BaseFragment {

    public static PatrolTaskDetailFrg newInstance(PatrolTask task) {
        PatrolTaskDetailFrg fragment = new PatrolTaskDetailFrg();
        fragment.name = "巡查任务详情";

        Bundle args = new Bundle();
        args.putSerializable("task", task);
        fragment.setArguments(args);

        return fragment;
    }


    private PatrolTask task;
    private EasyUiXml easyUiXml;
    private PropertyView propertyView;
    private LinearLayout llBottom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_com_form_layout);
    }

    @Override
    protected void initialize() {
        propertyView = $(R.id.propertyView);
        llBottom = $(R.id.llBottom);
        llBottom.setVisibility(View.GONE);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        if (getArguments() != null) task = (PatrolTask) getArguments().getSerializable("task");
        easyUiXml = IoXml.readXmlFromAssets(context, EasyUiXml.class, "patrolv1/XML_PATROL_MOUDLE_TASK.xml");
        Map map = ObjectUtil.Common.convert(task, Map.class);
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
}
