package com.titan.jnly.patrolv1.ui;

import android.os.Bundle;

import com.lhj.omnipotent.FunctionManager;
import com.titan.jnly.R;
import com.titan.jnly.common.uitl.Constant;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import androidx.appcompat.app.AppCompatActivity;

/*巡查消息详情页*/

public class PatrolNoticeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_notice_details);

        initData();
    }

    private void initData(){
        PatrolTask patrolTask = FunctionManager.getInstance().invokeFunction(Constant.FUN_PTASK_NOTICE,PatrolTask.class);
    }
}
