package com.titan.jnly.map.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.titan.jnly.R;

import java.io.Serializable;

/**
 * Created by zy on 2019/5/30.
 */

public class LocalDialog extends BaseDialogFrg implements View.OnClickListener {

    public static LocalDialog newInstance(ICallBack iCallBack) {
        LocalDialog fragment = new LocalDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", iCallBack);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView cancel, submit;
    private TextView cetLon, cetLat;
    private ICallBack iCallBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iCallBack = (ICallBack) getArguments().getSerializable("data");
        initTitle(null, "坐标定位", Gravity.CENTER);
        setContentView(R.layout.map_ui_dialog_loc);
        w = 0.85f;
        h = 0.5f;
    }

    @Override
    protected void initialize() {
        submit = $(R.id.submit);
        cancel = $(R.id.cancel);
        cetLon = $(R.id.cetLon);
        cetLat = $(R.id.cetLat);
    }

    @Override
    protected void registerEvent() {
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            if (iCallBack != null) {
                String s1 = cetLon.getText().toString();
                String s2 = cetLat.getText().toString();

                Double dLon = null;
                Double dLat = null;
                try {
                    dLon = Double.parseDouble(s1);
                    dLat = Double.parseDouble(s2);
                } catch (Exception e) {
                    dLon = null;
                    dLat = null;
                }
                if (dLon == null || dLat == null || dLon < -180 || dLon > 180 || dLat < -90 || dLat > 90) {
                    Toast.makeText(getContext(), "经度或纬度输入错误!", Toast.LENGTH_LONG).show();
                    return;
                }
                iCallBack.sure(dLon, dLat);
                dismiss();
            }
        }
        if (v.getId() == R.id.cancel) {
            dismiss();
        }
    }

    public interface ICallBack extends Serializable {
        public void sure(double dLon, double dLat);
    }
}
