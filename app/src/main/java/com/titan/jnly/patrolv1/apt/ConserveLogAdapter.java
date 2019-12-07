package com.titan.jnly.patrolv1.apt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.ConserveTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConserveLogAdapter extends RecyclerView.Adapter<ConserveLogAdapter.ConserveLogViewHolder> {

    private Context mContext;
    private List<ConserveTask> list = new ArrayList<>();
    private ConserveLogItemClickListener LogItemClickListener;

    public ConserveLogAdapter(Context context, List<ConserveTask> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ConserveLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_conserve_log, parent, false);
        ConserveLogViewHolder holder = new ConserveLogViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConserveLogViewHolder holder, int position) {
        ConserveTask Log = list.get(position);

        holder.dzbqh_view.setText(Log.getDZBQH());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ConserveLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView dzbqh_view;
        private TextView quxian_view;

        public ConserveLogViewHolder(@NonNull View itemView) {
            super(itemView);

            dzbqh_view = itemView.findViewById(R.id.view_dzbqh);
            quxian_view = itemView.findViewById(R.id.view_quxian);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(LogItemClickListener != null){
                LogItemClickListener.onItemClick(getPosition());
            }
        }
    }

    public interface ConserveLogItemClickListener {
        void onItemClick(int position);
    }

    public void sendClick(ConserveLogItemClickListener ConserveLogItemClickListener){
        this.LogItemClickListener = ConserveLogItemClickListener;
    }

}
