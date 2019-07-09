package com.titan.jnly.map.apt;

import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;

public class LayerNodeAdapter extends BaseRecycleAdapter<LayerNode, BaseViewHolder<LayerNode>> {

    public LayerNodeAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<LayerNode> onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


}
