package com.titan.jnly.task.apt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.data.Feature;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.utils.AppResUtil;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.NotifyArrayList;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.widget.easyui.utils.EasyUtil;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.titan.jnly.R;
import com.titan.jnly.system.Constant;
import com.titan.jnly.vector.bean.Species;
import com.titan.jnly.vector.enums.DataStatus;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class DataSyncAdapter extends BaseRecycleAdapter<Feature, BaseViewHolder<Feature>> {


    private NotifyArrayList<Integer> records;

    public DataSyncAdapter(@Nullable RecyclerView recyclerView, NotifyArrayList.IListener iListener) {
        super(recyclerView);
        recyclerView.addOnScrollListener(new ScrollListen());
        this.records = new NotifyArrayList<>(iListener);
    }


    @Override
    public BaseViewHolder<Feature> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.task_sync_list_item);
    }

    class Holder extends BaseViewHolder<Feature> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        CheckBox cbBox;
        TextView tvIndex, tvNum, tvName, tvDate, tvAge, tvStatus;
        ImageView ivSync;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            cbBox = $(R.id.cbBox);
            tvIndex = $(R.id.tvIndex);
            tvNum = $(R.id.tvNum);
            tvName = $(R.id.tvName);
            tvDate = $(R.id.tvDate);
            tvAge = $(R.id.tvAge);
            tvStatus = $(R.id.tvStatus);
            ivSync = $(R.id.ivSync);
            ivSync.setOnClickListener(this);
            cbBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void setData(Feature data, int position) {
            if (position % 2 != 0) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
            } else {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            tvIndex.setText(position + 1 + ".");

            if (records.contains(position)) {
                cbBox.setChecked(true);
            } else {
                cbBox.setChecked(false);
            }

            Map property = data.getAttributes();
            String sign = StringUtil.removeNull(property.get("DZBQH"));
            String code = StringUtil.removeNull(property.get("SZZWM"));
            String dateStr = StringUtil.removeNull(property.get("DCRQ"));
            GregorianCalendar calendar = (GregorianCalendar) property.get("DCRQ");
            if (calendar != null) {
                Date date = calendar.getTime();
                dateStr = DateUtil.dateTimeToStr(date);
            }
            dateStr = StringUtil.removeNull(dateStr);
            String age = StringUtil.removeNull(property.get("MODEL_AGE"));

            Short status = (Short) property.get(DataStatus.DATA_STATUS);
            DataStatus dataStatus = DataStatus.getEnum(status);
            Species species = Constant.getSpeciesByCode(code);

            tvNum.setText(sign);
            if (species != null) {
                tvName.setText(species.getSpecies());
            }
            tvDate.setText(dateStr);
            tvAge.setText(age);

            if (dataStatus == DataStatus.LOCAL_ADD) {
                tvStatus.setTextColor(AppResUtil.getColor(context, R.color.hx_red));
            }
            if (dataStatus == DataStatus.LOCAL_EDIT) {
                tvStatus.setTextColor(AppResUtil.getColor(context, R.color.hx_yellow));
            }
            if (dataStatus == DataStatus.REMOTE_SYNC) {
                tvStatus.setTextColor(AppResUtil.getColor(context, R.color.hx_green));
            }
            tvStatus.setText(dataStatus.getName());
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //非代码设置,主动触发
            if (buttonView.isPressed()) {
                if (isChecked) {
                    if (records.size() >= 10) {
                        buttonView.setChecked(false);
                        return;
                    }
                    if (!records.contains(position))
                        records.add(position);
                } else {
                    records.remove((Integer) position);
                }
            }
        }
    }

    /**
     * 获取选择的个数
     *
     * @return
     */
    public int getSelCount() {
        return records.size();
    }

    /**
     * 获取选中的数据
     *
     * @return
     */
    public List getSelData() {
        return getItems(records);
    }

    /**
     * 清空所有选中的数据
     */
    public void clearAllSel() {
        if (records.size() == 0) return;
        records.clear();
        notifyItemRangeChanged(firstItemPosition, lastItemPosition - firstItemPosition + 1);
    }

    /**
     * 更新list里的数据
     *
     * @param features
     */
    public void updateData(List<Feature> features) {

    }

    public void clearAllSelV1() {
        Holder holder;
        for (int i = 0; i < records.size(); i++) {
            holder = (Holder) getViewHolderByPosition(i);
            if (holder == null) continue;
            holder.cbBox.setChecked(false);
        }
        records.clear();
    }
}
