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
import com.titan.jnly.patrolv1.apt.ProtalNoticeAdapter;
import com.titan.jnly.patrolv1.bean.PatrolTask;
import com.titan.jnly.patrolv1.util.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/*巡查消息列表页*/
public class PatrolNoticeActivity extends AppCompatActivity implements ProtalNoticeAdapter.PatrolNoticeItemClickListener {

    private RecyclerView recyclerView;
    private TwinklingRefreshLayout refreshLayout;
    private List<PatrolTask> patrolTaskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_notice);

        initView();
        initAdapter();
        refresh();
    }

    private void initView(){
        recyclerView = findViewById(R.id.patrolnotice_recyclerview);
        refreshLayout = findViewById(R.id.refreshLayout);
    }

    private void initAdapter(){
        getData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerGridItemDecoration decoration = new DividerGridItemDecoration(this);
        ProtalNoticeAdapter adapter = new ProtalNoticeAdapter(this,patrolTaskList);
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
            PatrolTask task = new PatrolTask();
            task.setDZBQH(i+"");
            patrolTaskList.add(task);
        }
    }

    @Override
    public void onItemClick(int position) {
        PatrolTask patrolTask = patrolTaskList.get(position);
        if(patrolTask != null){
            FunctionManager.getInstance().addFunction(new FunctionNoParamHasResult<PatrolTask>(Constant.FUN_PTASK_NOTICE) {
                @Override
                public PatrolTask function() {
                    return patrolTask;
                }
            });

            startActivity(new Intent(PatrolNoticeActivity.this,PatrolNoticeDetailsActivity.class));
        }
    }
}
