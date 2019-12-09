package com.titan.jnly.patrolv1.ui.frg;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lib.bandaid.fragment.BaseFragment;
import com.titan.jnly.R;

public class PatrolTaskMsgFrg extends BaseFragment {

    public static PatrolTaskMsgFrg newInstance() {
        PatrolTaskMsgFrg fragment = new PatrolTaskMsgFrg();
        fragment.name = "巡查消息";
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_aty_patrol_log);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {

    }
}
