package com.titan.jnly.patrolv1.apt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.PatrolMsg;

public class PatrolMsgApt extends BaseRecycleAdapter<PatrolMsg, BaseViewHolder<PatrolMsg>> {

    public PatrolMsgApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<PatrolMsg> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.patrolv1_apt_patro_notice_item);
    }

    class Holder extends BaseViewHolder<PatrolMsg> implements View.OnClickListener {

        private TextView dzbqh_view;
        private TextView quxian_view;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);

            dzbqh_view = itemView.findViewById(R.id.view_dzbqh);
            quxian_view = itemView.findViewById(R.id.view_quxian);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(PatrolMsg data, int position) {

        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }
}
