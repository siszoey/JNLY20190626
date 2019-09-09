package com.titan.jnly.common.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.lib.bandaid.arcruntime.core.draw.DrawType;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.titan.jnly.R;

import java.io.Serializable;

/**
 * Created by zy on 2019/6/10.
 */

public class DrawDialog extends BaseDialogFrg implements View.OnClickListener {

    public static DrawDialog newInstance(ICallBack iCallBack) {
        DrawDialog fragment = new DrawDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("callback", iCallBack);
        fragment.setArguments(bundle);
        return fragment;
    }


    private ImageView ivDrawCircle, ivDrawRect, ivDrawPolygon;
    private ICallBack iCallBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iCallBack = (ICallBack) getArguments().getSerializable("callback");
        initTitle(R.drawable.ic_close, "绘制类型", Gravity.CENTER);
        setContentView(R.layout.com_ui_dialog_draw);
    }

    @Override
    protected void initialize() {
        ivDrawCircle = $(R.id.ivDrawCircle);
        ivDrawRect = $(R.id.ivDrawRect);
        ivDrawPolygon = $(R.id.ivDrawPolygon);
    }

    @Override
    protected void registerEvent() {
        ivDrawCircle.setOnClickListener(this);
        ivDrawRect.setOnClickListener(this);
        ivDrawPolygon.setOnClickListener(this);
    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivDrawCircle) {
            if (iCallBack != null) iCallBack.callback(DrawType.CIRCLE);
        }
        if (v.getId() == R.id.ivDrawRect) {
            if (iCallBack != null) iCallBack.callback(DrawType.ENVELOPE);
        }
        if (v.getId() == R.id.ivDrawPolygon) {
            if (iCallBack != null) iCallBack.callback(DrawType.FREEHAND_POLYGON);
        }
        dismiss();
    }

    public interface ICallBack extends Serializable {
        void callback(Object o);
    }
}
