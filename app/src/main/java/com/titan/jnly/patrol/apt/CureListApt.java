package com.titan.jnly.patrol.apt;

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
import com.titan.jnly.patrol.bean.CureModel;

import java.util.Map;

public class CureListApt extends BaseRecycleAdapter<CureModel, BaseViewHolder<CureModel>> {

    public CureListApt(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<CureModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.patrol_apt_cure_list_item);
    }

    class Holder extends BaseViewHolder<CureModel> implements View.OnClickListener {

        TextView tvIndex, tvNum, tvOrder, tvName, tvDate;
        ImageView ivEdit, ivDel;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);

            tvIndex = $(R.id.tvIndex);
            tvNum = $(R.id.tvNum);
            tvOrder = $(R.id.tvOrder);
            tvName = $(R.id.tvName);
            tvDate = $(R.id.tvDate);
            ivEdit = $(R.id.ivEdit);
            ivDel = $(R.id.ivDel);

            itemView.setOnClickListener(this);
            ivEdit.setOnClickListener(this);
            ivDel.setOnClickListener(this);
        }

        @Override
        public void setData(CureModel data, int position) {
            //养护信息列表
            Map map = data.getMaintainRecord();
            tvIndex.setText(position + 1 + ". ");
            //电子标签号
            tvNum.setText(ObjectUtil.removeNull(map.get("DZBQH")));
            //调查序号
            tvOrder.setText(ObjectUtil.removeNull(map.get("DZBQH")));
            //古树名称
            tvName.setText(ObjectUtil.removeNull(map.get("SZZWM")));
            //养护日期
            tvDate.setText(ObjectUtil.removeNull(map.get("MaintainDate")));
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }

    public void updateItem(CureModel model) {
        changeItem(index(model), model);
    }

    private int index(CureModel model) {
        CureModel item;
        String itemId, modelId;
        modelId = (String) model.getMaintainRecord().get("Id");
        for (int i = 0; i < dataList.size(); i++) {
            item = dataList.get(i);
            itemId = (String) item.getMaintainRecord().get("Id");
            if (itemId.equals(modelId)) return i;
        }
        return -1;
    }
}
