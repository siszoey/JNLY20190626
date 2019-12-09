package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;

import com.titan.jnly.R;

import androidx.appcompat.app.AppCompatActivity;

/*巡查日志详情页*/

public class PatrolLogAty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_aty_patrol_task_details);

        initData();
    }

    private void initData() {
        // PatrolTask patrolTask = FunctionManager.getInstance().invokeFunction(Constant.FUN_PTASK_LOG,PatrolTask.class);
    }
}
