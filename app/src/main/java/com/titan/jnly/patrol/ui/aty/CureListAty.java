package com.titan.jnly.patrol.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.util.OSerial;
import com.titan.jnly.R;
import com.titan.jnly.examine.ui.aty.DataScanAty;

public class CureListAty extends BaseMvpCompatAty implements View.OnClickListener {

    private TextView tvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrol_ui_aty_cure_list_layout);
    }

    @Override
    protected void initialize() {
        tvDetail = $(R.id.tvDetail);
    }

    @Override
    protected void registerEvent() {
        tvDetail.setOnClickListener(this);
    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDetail) {
            /*Intent intent = new Intent(_context, DataScanAty.class);
            OSerial.putSerial(intent,null);
            startActivity(intent);*/
        }
    }
}
