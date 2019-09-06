package com.titan.jnly.login.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lib.bandaid.util.ViewUtil;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.titan.jnly.R;
import com.titan.jnly.examine.ui.aty.ExamineAty;
import com.titan.jnly.invest.ui.aty.InvestActivity;

public class DialogMould extends BaseDialogFrg implements View.OnClickListener {

    public static DialogMould newInstance() {
        DialogMould fragment = new DialogMould();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView tvInvest, tvCheck;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        h = ViewUtil.WRAP_CONTENT;
        setContentView(R.layout.moudle_invest_choice_dialog);
    }

    @Override
    protected void initialize() {
        tvInvest = $(R.id.tvInvest);
        tvCheck = $(R.id.tvCheck);
    }

    @Override
    protected void registerEvent() {
        tvInvest.setOnClickListener(this);
        tvCheck.setOnClickListener(this);
    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvInvest) {
            startActivity(new Intent(context, InvestActivity.class));
        }
        if (v.getId() == R.id.tvCheck) {
            startActivity(new Intent(context, ExamineAty.class));
        }
        dismiss();
    }
}
