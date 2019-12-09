package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.titan.jnly.R;

/*巡查任务详情页*/
public class PatrolTaskAty extends BaseMvpCompatAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "巡查任务详情", Gravity.CENTER);
        setContentView(R.layout.patrolv1_ui_aty_patrol_task_details);
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
