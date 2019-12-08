package com.titan.jnly.patrolv1.ui.aty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.titan.jnly.R;

/*巡查任务详情页*/

public class PatrolTaskAty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_task_details);

        initData();
    }

    private void initData(){
        //PatrolTask patrolTask = FunctionManager.getInstance().invokeFunction(Constant.FUN_GET_PTASK,PatrolTask.class);
    }
}
