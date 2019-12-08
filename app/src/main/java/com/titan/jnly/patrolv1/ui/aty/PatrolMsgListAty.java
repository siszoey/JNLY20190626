package com.titan.jnly.patrolv1.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.apt.PatrolMsgApt;
import com.titan.jnly.patrolv1.bean.PatrolMsg;

import androidx.recyclerview.widget.RecyclerView;


/*巡查消息列表页*/
public class PatrolMsgListAty extends BaseMvpCompatAty
        implements BaseRecycleAdapter.IViewClickListener<PatrolMsg> {

    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PatrolMsgApt patrolNoticeApt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "巡查消息", Gravity.CENTER);
        setContentView(R.layout.activity_patrol_notice);
    }

    @Override
    protected void initialize() {
        rvList = $(R.id.rvList);
        swipeLayout = $(R.id.swipeLayout);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        patrolNoticeApt = new PatrolMsgApt(rvList);
        patrolNoticeApt.setIViewClickListener(this);
    }

    @Override
    public void onClick(View view, PatrolMsg data, int position) {
        startActivity(new Intent(PatrolMsgListAty.this, PatrolMsgAty.class));
    }
}
