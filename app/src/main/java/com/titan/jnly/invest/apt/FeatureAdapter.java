package com.titan.jnly.invest.apt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.data.Feature;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.util.StringUtil;
import com.titan.jnly.R;

import java.util.Map;

public class FeatureAdapter extends BaseRecycleAdapter<GroupItem<Feature>, BaseViewHolder<GroupItem<Feature>>> {

    public FeatureAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<GroupItem<Feature>> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.map_ui_dialog_feature_item);
    }

    class Holder extends BaseViewHolder<GroupItem<Feature>> implements View.OnClickListener {

        TextView tvName;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            tvName = $(R.id.tvName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(GroupItem<Feature> data, int position) {
            Map map = data.getData().getAttributes();
            tvName.setText("图斑编号:" + StringUtil.removeNull(map.get("objectid")));
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }

    public View initGroupView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.map_ui_dialog_feature_head, null, false);
        TextView name = view.findViewById(R.id.tvName);
        String val = getItem(position).getName();
        name.setText(val);
        return view;
    }
}
