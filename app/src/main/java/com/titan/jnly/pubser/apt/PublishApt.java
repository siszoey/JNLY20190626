package com.titan.jnly.pubser.apt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.titan.jnly.R;
import com.titan.jnly.pubser.bean.Publish;


public class PublishApt extends BaseRecycleAdapter<Publish, BaseViewHolder<Publish>> {

    public PublishApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<Publish> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.pubser_apt_main_item_layout);
    }

    class Holder extends BaseViewHolder<Publish> implements View.OnClickListener {

        private TextView tvTitle, tvDate, tvPublisher;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            tvTitle = $(R.id.tvTitle);
            tvDate = $(R.id.tvDate);
            tvPublisher = $(R.id.tvPublisher);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(Publish data, int position) {
            tvTitle.setText(data.getTitle());
            tvDate.setText(data.getDate());
            tvPublisher.setText(data.getPublisher());
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }
}
