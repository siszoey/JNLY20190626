package com.titan.jnly.patrolv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lhj.omnipotent.FunctionManager;
import com.lhj.omnipotent.FunctionNoParamHasResult;
import com.titan.jnly.R;
import com.titan.jnly.common.uitl.Constant;
import com.titan.jnly.patrolv1.apt.ConserveLogAdapter;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.titan.jnly.patrolv1.util.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ConserveLogActivity extends AppCompatActivity implements ConserveLogAdapter.ConserveLogItemClickListener {


    private RecyclerView recyclerView;
    private TwinklingRefreshLayout refreshLayout;
    private List<ConserveTask> conserveLogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conserve_log);

        initView();
        initAdapter();
        refresh();
    }

    private void initView(){
        recyclerView = findViewById(R.id.conserve_log_recyclerview);
        refreshLayout = findViewById(R.id.refreshLayout);
    }

    private void initAdapter(){
        getData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(this);
        ConserveLogAdapter adapter = new ConserveLogAdapter(this,conserveLogList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        adapter.sendClick(this);
    }

    private void refresh(){
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }
        });
    }

    private void  getData(){
        for(int i=0;i<10;i++){
            ConserveTask Log = new ConserveTask();
            Log.setDZBQH(i+"");
            conserveLogList.add(Log);
        }
    }

    @Override
    public void onItemClick(int position) {
        ConserveTask conserveLog = conserveLogList.get(position);
        if(conserveLog != null){
            FunctionManager.getInstance().addFunction(new FunctionNoParamHasResult<ConserveTask>(Constant.FUN_CTASK_LOG) {
                @Override
                public ConserveTask function() {
                    return conserveLog;
                }
            });

            startActivity(new Intent(ConserveLogActivity.this,ConserveLogDetailsActivity.class));
        }
    }
}
