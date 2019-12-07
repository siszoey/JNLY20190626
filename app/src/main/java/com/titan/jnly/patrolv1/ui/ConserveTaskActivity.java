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
import com.titan.jnly.patrolv1.apt.ConserveTaskAdapter;
import com.titan.jnly.patrolv1.bean.ConserveTask;
import com.titan.jnly.patrolv1.util.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ConserveTaskActivity extends AppCompatActivity implements ConserveTaskAdapter.ConserveTaskItemClickListener {


    private RecyclerView recyclerView;
    private TwinklingRefreshLayout refreshLayout;
    private List<ConserveTask> conserveTaskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conserve_task);

        initView();
        initAdapter();
        refresh();
    }

    private void initView(){
        recyclerView = findViewById(R.id.conserve_task_recyclerview);
        refreshLayout = findViewById(R.id.refreshLayout);
    }

    private void initAdapter(){
        getData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(this);
        ConserveTaskAdapter adapter = new ConserveTaskAdapter(this,conserveTaskList);
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
            ConserveTask task = new ConserveTask();
            task.setDZBQH(i+"");
            conserveTaskList.add(task);
        }
    }

    @Override
    public void onItemClick(int position) {
        ConserveTask conserveTask = conserveTaskList.get(position);
        if(conserveTask != null){
            FunctionManager.getInstance().addFunction(new FunctionNoParamHasResult<ConserveTask>(Constant.FUN_GET_CTASK) {
                @Override
                public ConserveTask function() {
                    return conserveTask;
                }
            });

            startActivity(new Intent(ConserveTaskActivity.this,ConserveTaskDetailsActivity.class));
        }
    }
}
