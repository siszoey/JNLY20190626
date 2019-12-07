package com.titan.jnly.patrolv1.ui;

import android.os.Bundle;

import com.lhj.omnipotent.FunctionManager;
import com.titan.jnly.R;
import com.titan.jnly.common.uitl.Constant;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import androidx.appcompat.app.AppCompatActivity;

public class ConserveTaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conserve_task_details);

        initData();
    }

    private void initData(){
        ConserveTask conserveTask = FunctionManager.getInstance().invokeFunction(Constant.FUN_GET_CTASK,ConserveTask.class);
    }
}
