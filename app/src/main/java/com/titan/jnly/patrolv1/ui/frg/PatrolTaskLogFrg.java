package com.titan.jnly.patrolv1.ui.frg;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lib.bandaid.fragment.BaseFragment;
import com.titan.jnly.R;

public class PatrolTaskLogFrg extends BaseFragment {

    public static PatrolTaskLogFrg newInstance() {
        PatrolTaskLogFrg fragment = new PatrolTaskLogFrg();
        fragment.name = "巡查日志";
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
