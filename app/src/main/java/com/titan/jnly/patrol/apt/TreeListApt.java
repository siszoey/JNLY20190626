package com.titan.jnly.patrol.apt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.util.StringUtil;
import com.titan.jnly.R;

import java.util.Map;

public class TreeListApt extends BaseRecycleAdapter<Map, BaseViewHolder<Map>> {

    public TreeListApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<Map> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.patrol_apt_data_list_item);
    }

    class Holder extends BaseViewHolder<Map> implements View.OnClickListener {

        TextView tvIndex, tvNum, tvOrder, tvName, tvDate, tvAge;
        ImageView ivPatrol,ivCure;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);

            tvIndex = $(R.id.tvIndex);
            tvNum = $(R.id.tvNum);
            tvOrder = $(R.id.tvOrder);
            tvName = $(R.id.tvName);
            tvDate = $(R.id.tvDate);
            tvAge = $(R.id.tvAge);
            ivPatrol = $(R.id.ivPatrol);
            ivCure = $(R.id.ivCure);


            itemView.setOnClickListener(this);
            ivPatrol.setOnClickListener(this);
            ivCure.setOnClickListener(this);
        }

        @Override
        public void setData(Map property, int position) {
            tvIndex.setText(position + 1 + ".");
            String sign = StringUtil.removeNull(property.get("DZBQH"));
            String order = StringUtil.removeNull(property.get("DCSXH"));
            String species = StringUtil.removeNull(property.get("SZZWM"));
            String dateStr = StringUtil.removeNull(property.get("DCRQ"));
            dateStr = StringUtil.removeNull(dateStr);
            String age = StringUtil.removeNull(property.get("MODEL_AGE"));

            tvNum.setText(sign);
            tvOrder.setText(order);
            tvName.setText(species);
            tvDate.setText(dateStr);
            tvAge.setText(age);
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }
}
