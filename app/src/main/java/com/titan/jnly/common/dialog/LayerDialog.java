package com.titan.jnly.common.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.titan.jnly.R;
import com.titan.jnly.common.apt.LayerAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * 图层选择（在操作图斑时候，如果需要，就弹出对话框，让用户选择数据）
 */
public class LayerDialog extends BaseDialogFrg
        implements BaseRecycleAdapter.IViewClickListener<LayerNode> {

    public static LayerDialog newInstance() {
        LayerDialog fragment = new LayerDialog();
        return fragment;
    }

    private ICallBack iCallBack;
    private List<LayerNode> layerNodes;
    private RecyclerView rvLayers;
    private LayerAdapter layerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, "选择图层", Gravity.CENTER);
        h = 0.5f;
        setContentView(R.layout.com_ui_dialog_layer_sel);
    }

    public LayerDialog setCallBack(List<LayerNode> layerNodes,ICallBack iCallBack) {
        this.layerNodes = layerNodes;
        this.iCallBack = iCallBack;
        return this;
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
        layerAdapter = new LayerAdapter(rvLayers);
        layerAdapter.setIViewClickListener(this);
        layerAdapter.replaceAll(layerNodes);
    }

    @Override
    public void onClick(View view, LayerNode data, int position) {
        if (iCallBack != null) iCallBack.callBack(data);
        dismiss();
    }

    public interface ICallBack extends Serializable {
        public void callBack(LayerNode layerNode);
    }
}
