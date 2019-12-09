package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.titan.jnly.R;

import androidx.appcompat.app.AppCompatActivity;

/*巡查日志详情页*/

public class PatrolLogAty extends BaseMvpCompatAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "巡查日志", Gravity.CENTER);
        setContentView(R.layout.patrolv1_ui_com_form_layout);

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
