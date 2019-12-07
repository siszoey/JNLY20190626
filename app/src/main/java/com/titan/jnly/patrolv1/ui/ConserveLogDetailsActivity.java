package com.titan.jnly.patrolv1.ui;

import android.os.Bundle;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lhj.omnipotent.FunctionManager;
import com.titan.jnly.R;
import com.titan.jnly.common.uitl.Constant;
import com.titan.jnly.patrolv1.bean.ConserveTask;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ConserveLogDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conserve_log_details);

        initData();
    }

    private void initData(){
        ConserveTask conserveTask = FunctionManager.getInstance().invokeFunction(Constant.FUN_CTASK_LOG,ConserveTask.class);
    }
}
