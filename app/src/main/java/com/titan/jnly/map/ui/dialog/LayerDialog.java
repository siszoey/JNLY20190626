package com.titan.jnly.map.ui.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.titan.jnly.R;

/**
 * 图层选择（在操作图斑时候，如果需要，就弹出对话框，让用户选择数据）
 */
public class LayerDialog extends BaseDialogFrg implements View.OnClickListener {

    public static LayerDialog newInstance() {
        LayerDialog fragment = new LayerDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("callback", null);
        fragment.setArguments(bundle);
        return fragment;
    }

    private RecyclerView rvLayers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_ui_dialog_layer_sel);
    }

    @Override
    protected void initialize() {
        rvLayers = $(R.id.rvLayers);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View v) {

    }
}
