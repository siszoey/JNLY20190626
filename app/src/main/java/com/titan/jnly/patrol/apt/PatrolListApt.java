package com.titan.jnly.patrol.apt;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.util.ObjectUtil;
import com.titan.jnly.R;
import com.titan.jnly.patrol.bean.PatrolModel;

import java.util.Map;

public class PatrolListApt extends BaseRecycleAdapter<PatrolModel, BaseViewHolder<PatrolModel>> {


    public PatrolListApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<PatrolModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.patrol_apt_patrol_list_item);
    }

    class Holder extends BaseViewHolder<PatrolModel> implements View.OnClickListener {

        TextView tvIndex, tvNum, tvOrder, tvName, tvDate, tvLevel;
        ImageView ivEdit, ivDel;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            tvIndex = $(R.id.tvIndex);
            tvNum = $(R.id.tvNum);
            tvOrder = $(R.id.tvOrder);
            tvName = $(R.id.tvName);
            tvDate = $(R.id.tvDate);
            tvLevel = $(R.id.tvLevel);
            ivEdit = $(R.id.ivEdit);
            ivDel = $(R.id.ivDel);

            itemView.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            ivDel.setOnClickListener(this);
        }


        @Override
        public void setData(PatrolModel data, int position) {
            //养护信息列表
            Map map = data.getPatrolRecord();

            tvIndex.setText(position + 1 + ". ");
            //电子标签号
            tvNum.setText(ObjectUtil.removeNull(map.get("DZBQH")));
            //调查序号
            tvOrder.setText(ObjectUtil.removeNull(map.get("DZBQH")));
            //古树名称
            tvName.setText(ObjectUtil.removeNull(map.get("SZZWM")));
            //巡查日期
            tvDate.setText(ObjectUtil.removeNull(map.get("PatrolDate")));
            //巡查等级
            Object objLevel = map.get("PatrolLevel");
            Integer level = null;
            if (objLevel instanceof String) {
                level = Integer.parseInt(objLevel + "");
            }
            if (objLevel instanceof Double) {
                level = ((Double) objLevel).intValue();
            }
            if (level == null) return;
            if (level.intValue() == 1) {
                tvLevel.setText("正常");
                tvLevel.setTextColor(Color.GREEN);
            }
            if (level.intValue() == 2) {
                tvLevel.setText("较重");
                tvLevel.setTextColor(Color.CYAN);
            }
            if (level.intValue() == 3) {
                tvLevel.setText("严重");
                tvLevel.setTextColor(Color.parseColor("#FF8000"));
            }
            if (level.intValue() == 4) {
                tvLevel.setText("紧急");
                tvLevel.setTextColor(Color.RED);
            }
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }

    public void updateItem(PatrolModel model) {
        changeItem(index(model), model);
    }

    private int index(PatrolModel model) {
        PatrolModel item;
        String itemId, modelId;
        modelId = (String) model.getPatrolRecord().get("Id");
        for (int i = 0; i < dataList.size(); i++) {
            item = dataList.get(i);
            itemId = (String) item.getPatrolRecord().get("Id");
            if (itemId.equals(modelId)) return i;
        }
        return -1;
    }
}
