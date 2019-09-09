package com.titan.jnly.common.apt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.util.StringUtil;
import com.titan.jnly.R;

import java.util.Map;

/**
 * 历史轨迹Adapter
 */
public class TrackAdapter extends BaseRecycleAdapter<Map, BaseViewHolder<Map>> {

    public TrackAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<Map> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.com_apt_track_layout);
    }

    class Holder extends BaseViewHolder<Map> implements View.OnClickListener {

        TextView tvTrackId, tvSpotCount, tvStartTime, tvEndTime;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            tvTrackId = $(R.id.tvTrackId);
            tvSpotCount = $(R.id.tvSpotCount);
            tvStartTime = $(R.id.tvStartTime);
            tvEndTime = $(R.id.tvEndTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(Map data, int position) {
            if (data == null) return;
            tvTrackId.setText(StringUtil.removeNull(data.get("trackId")));
            tvSpotCount.setText(StringUtil.removeNull(data.get("count")));
            tvStartTime.setText(StringUtil.removeNull(data.get("startTime")));
            tvEndTime.setText(StringUtil.removeNull(data.get("endTime")));
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }
}
