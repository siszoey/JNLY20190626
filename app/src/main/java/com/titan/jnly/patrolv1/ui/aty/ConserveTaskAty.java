package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import com.titan.jnly.R;

import androidx.appcompat.app.AppCompatActivity;

public class ConserveTaskAty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrolv1_ui_aty_conserve_task_details);

        initData();
    }

    private void initData(){
        //ConserveTask conserveTask = FunctionManager.getInstance().invokeFunction(Constant.FUN_GET_CTASK,ConserveTask.class);
    }
}
