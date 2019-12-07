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

public class ConserveTaskAdapter extends RecyclerView.Adapter<ConserveTaskAdapter.ConserveTaskViewHolder> {

    private Context mContext;
    private List<ConserveTask> list = new ArrayList<>();
    private ConserveTaskItemClickListener taskItemClickListener;

    public ConserveTaskAdapter(Context context, List<ConserveTask> list){
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ConserveTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_conserve_task, parent, false);
        ConserveTaskViewHolder holder = new ConserveTaskViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConserveTaskViewHolder holder, int position) {
        ConserveTask task = list.get(position);

        holder.dzbqh_view.setText(task.getDZBQH());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ConserveTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView dzbqh_view;
        private TextView quxian_view;

        public ConserveTaskViewHolder(@NonNull View itemView) {
            super(itemView);

            dzbqh_view = itemView.findViewById(R.id.view_dzbqh);
            quxian_view = itemView.findViewById(R.id.view_quxian);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(taskItemClickListener != null){
                taskItemClickListener.onItemClick(getPosition());
            }
        }
    }

    public interface ConserveTaskItemClickListener {
        void onItemClick(int position);
    }

    public void sendClick(ConserveTaskItemClickListener ConserveTaskItemClickListener){
        this.taskItemClickListener = ConserveTaskItemClickListener;
    }

}
