package com.titan.jnly.examine.apt;

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

public class DataListApt extends BaseRecycleAdapter<Map, BaseViewHolder<Map>> {

    public DataListApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<Map> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.exam_apt_data_list_item);
    }

    class Holder extends BaseViewHolder<Map> implements View.OnClickListener {

        TextView tvIndex, tvNum, tvOrder, tvName, tvDate, tvAge, tvStatus;
        ImageView ivNav;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);

            tvIndex = $(R.id.tvIndex);
            tvNum = $(R.id.tvNum);
            tvOrder = $(R.id.tvOrder);
            tvName = $(R.id.tvName);
            tvDate = $(R.id.tvDate);
            tvAge = $(R.id.tvAge);
            tvStatus = $(R.id.tvStatus);
            ivNav = $(R.id.ivNav);


            itemView.setOnClickListener(this);
            ivNav.setOnClickListener(this);
        }

        @Override
        public void setData(Map property, int position) {
            /*if (position % 2 != 0) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
            } else {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }*/
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
