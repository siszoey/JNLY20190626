package com.titan.jnly.patrolv1.apt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.titan.jnly.R;
import com.titan.jnly.patrolv1.bean.PatrolTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProtalLogAdapter extends RecyclerView.Adapter<ProtalLogAdapter.ProtalLogViewHolder> {

    private Context mContext;
    private List<PatrolTask> list = new ArrayList<>();
    private PatrolLogItemClickListener logItemClickListener;

    public ProtalLogAdapter(Context context, List<PatrolTask> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProtalLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_patro_log, parent, false);
        ProtalLogViewHolder holder = new ProtalLogViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProtalLogViewHolder holder, int position) {
        PatrolTask task = list.get(position);

        holder.dzbqh_view.setText(task.getDZBQH());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProtalLogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView dzbqh_view;
        private TextView quxian_view;

        public ProtalLogViewHolder(@NonNull View itemView) {
            super(itemView);

            dzbqh_view = itemView.findViewById(R.id.view_dzbqh);
            quxian_view = itemView.findViewById(R.id.view_quxian);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(logItemClickListener != null){
                logItemClickListener.onItemClick(getPosition());
            }
        }
    }

    public interface PatrolLogItemClickListener {
        void onItemClick(int position);
    }

    public void sendClick(PatrolLogItemClickListener patrolLogItemClickListener){
        this.logItemClickListener = logItemClickListener;
    }

}
