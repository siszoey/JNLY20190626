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
import com.titan.jnly.patrolv1.bean.ConserveLog;
import com.titan.jnly.patrolv1.bean.ConserveTask;


public class ConserveLogApt extends BaseRecycleAdapter<ConserveLog, BaseViewHolder<ConserveLog>> {

    public ConserveLogApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<ConserveLog> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(context, R.layout.patrolv1_apt_conserve_log_item);
    }

    class Holder extends BaseViewHolder<ConserveLog> implements View.OnClickListener {


        public Holder(Context context, int resId) {
            super(context, resId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(ConserveLog data, int position) {

        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }
}
