package com.titan.jnly.common.apt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.geometry.GeometryType;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.titan.jnly.R;

public class LayerAdapter extends BaseRecycleAdapter<LayerNode, BaseViewHolder<LayerNode>> {

    public LayerAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<LayerNode> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.com_ui_dialog_layer_item);
    }

    class Holder extends BaseViewHolder<LayerNode> implements View.OnClickListener {

        ImageView ivIcon;
        TextView tvName;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            tvName = $(R.id.tvName);
            ivIcon = $(R.id.ivIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(LayerNode data, int position) {
            tvName.setText(data.getName());
            if (data.tryGetGeometryType() == null) return;
            if (data.tryGetGeometryType() == GeometryType.POINT || data.tryGetGeometryType() == GeometryType.MULTIPOINT) {
                ivIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_layer_point));
            }
            if (data.tryGetGeometryType() == GeometryType.POLYLINE) {
                ivIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_layer_line));
            }
            if (data.tryGetGeometryType() == GeometryType.POLYGON) {
                ivIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_layer_polygon));
            }
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }

}
