package com.titan.jnly.patrol.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.PageParam;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.util.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.titan.jnly.R;
import com.titan.jnly.examine.ui.aty.DataScanAty;
import com.titan.jnly.patrol.api.IPatrolCure;

import java.util.List;
import java.util.Map;

/**
 * 养护巡查业务列表
 */
public class PatrolListAty extends BaseMvpCompatAty implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {

    protected Map treeData;
    private TextView tvNum, tvOrder, tvName, tvDate, tvAge, tvDetail;
    private RecyclerView rvList;
    private SmartRefreshLayout swipeLayout;
    private PageParam pageParam = PageParam.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) treeData = OSerial.getData(getIntent(), Map.class);
        initTitle(R.drawable.ic_back, "巡查", Gravity.CENTER);
        setContentView(R.layout.patrol_ui_aty_patrol_list_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_right_add).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_right_add) {
            Intent intent = new Intent(_context, PatrolItemAty.class);
            OSerial.putSerial(intent, treeData);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initialize() {
        tvDetail = $(R.id.tvDetail);
        tvNum = $(R.id.tvNum);
        tvOrder = $(R.id.tvOrder);
        tvName = $(R.id.tvName);
        tvDate = $(R.id.tvDate);
        tvAge = $(R.id.tvAge);
        rvList = $(R.id.rvList);

        swipeLayout = $(R.id.swipeLayout);
    }

    @Override
    protected void registerEvent() {
        tvDetail.setOnClickListener(this);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void initClass() {
        treeInfo();
        queryData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDetail) {
            Intent intent = new Intent(_context, DataScanAty.class);
            OSerial.putSerial(intent, treeData);
            startActivity(intent);
        }
    }

    void treeInfo() {
        if (treeData == null) return;
        String sign = StringUtil.removeNull(treeData.get("DZBQH"));
        pageParam.pushOtherParam("eleCode", sign);
        String order = StringUtil.removeNull(treeData.get("DCSXH"));
        String species = StringUtil.removeNull(treeData.get("SZZWM"));
        String dateStr = StringUtil.removeNull(treeData.get("DCRQ"));
        dateStr = StringUtil.removeNull(dateStr);
        String age = StringUtil.removeNull(treeData.get("MODEL_AGE"));

        tvNum.setText(sign);
        tvOrder.setText(order);
        tvName.setText(species);
        tvDate.setText(dateStr);
        tvAge.setText(age);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageParam.New();
        queryData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageParam.next();
        queryData();
    }

    void queryData() {
        String eleCode = pageParam.getOtherParam("eleCode");
        netEasyReq.request(IPatrolCure.class, new NetWorkListen<TTResult<Map>>() {
            @Override
            public void onSuccess(TTResult<Map> data) {
                List<Map> list = ObjectUtil.convert(data.getContent().get("rows"), Map.class);
                if (pageParam.isNew()) {

                }
                if (pageParam.isMore()) {

                }
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                if (pageParam.isNew()) {

                }
                if (pageParam.isMore()) {

                }
            }
        }).httpGetPatrolList(eleCode, pageParam.getNum(), pageParam.getSize());
    }
}
