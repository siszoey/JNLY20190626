package com.titan.jnly.patrolv1.ui.frg;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lib.bandaid.fragment.BaseFragment;
import com.lib.bandaid.rw.file.xml.IoXml;
import com.lib.bandaid.widget.easyui.convert.Resolution;
import com.lib.bandaid.widget.easyui.ui_v1.ILifeCycle;
import com.lib.bandaid.widget.easyui.ui_v1.PropertyView;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.zy.foxui.util.ObjectUtil;

import java.util.Map;

public class ConserveTaskDetailFrg extends BaseFragment {

    public static ConserveTaskDetailFrg newInstance(ConserveTask task) {
        ConserveTaskDetailFrg fragment = new ConserveTaskDetailFrg();
        fragment.name = "施工任务详情";

        Bundle args = new Bundle();
        args.putSerializable("task", task);
        fragment.setArguments(args);

        return fragment;
    }

    private ConserveTask task;
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
        if (getArguments() != null) task = (ConserveTask) getArguments().getSerializable("task");
        easyUiXml = IoXml.readXmlFromAssets(context, EasyUiXml.class, "patrolv1/XML_CONSERVE_MOUDLE_TASK.xml");
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
