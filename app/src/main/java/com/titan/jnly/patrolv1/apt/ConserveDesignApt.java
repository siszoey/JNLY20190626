package com.titan.jnly.patrolv1.apt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.ConserveTask;

public class ConserveDesignApt extends BaseRecycleAdapter<ConserveTask, BaseViewHolder<ConserveTask>> {

    public ConserveDesignApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<ConserveTask> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(context, R.layout.item_conserve_log);
    }

    class Holder extends BaseViewHolder<ConserveTask> implements View.OnClickListener {

        private TextView dzbqh_view;
        private TextView quxian_view;

        public Holder(Context context, int resId) {
            super(context, resId);
            dzbqh_view = $(R.id.view_dzbqh);
            quxian_view = $(R.id.view_quxian);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(ConserveTask data, int position) {
            dzbqh_view.setText(data.getDZBQH());
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }
}
