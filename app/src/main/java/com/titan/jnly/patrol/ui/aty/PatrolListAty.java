package com.titan.jnly.patrol.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.util.StringUtil;
import com.titan.jnly.R;
import com.titan.jnly.examine.ui.aty.DataScanAty;
import com.titan.jnly.patrol.api.IPatrol;

import java.util.List;
import java.util.Map;

/**
 * 养护巡查业务列表
 */
public class PatrolListAty extends BaseMvpCompatAty implements View.OnClickListener {

    protected Map treeData;
    private TextView tvNum, tvOrder, tvName, tvDate, tvAge, tvDetail;
    private RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) treeData = OSerial.getData(getIntent(), Map.class);
        initTitle(R.drawable.ic_back, "巡查", Gravity.CENTER);
        setContentView(R.layout.patrol_ui_aty_patrol_list_layout);
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

        Map param = SimpleMap.create();
        netEasyReq.request(IPatrol.class, new NetWorkListen<TTResult<Map>>() {
            @Override
            public void onSuccess(TTResult<Map> data) {
                List<Map> list = ObjectUtil.convert(data.getContent().get("rows"), Map.class);
                System.out.println(list);
            }
        }).httpList(param);
    }

    @Override
    protected void registerEvent() {
        tvDetail.setOnClickListener(this);

    }

    @Override
    protected void initClass() {
        if (treeData != null) {
            String sign = StringUtil.removeNull(treeData.get("DZBQH"));
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

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDetail) {
            Intent intent = new Intent(_context, DataScanAty.class);
            OSerial.putSerial(intent, treeData);
            startActivity(intent);
        }
    }
}
