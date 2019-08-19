package com.lib.bandaid.adapter.com;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.R;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SimpleRvAdapter<T> extends BaseRecycleAdapter<T, BaseViewHolder<T>> {

    private int lastSelIndex = -1;
    private boolean isMulti = true;
    private IFillData<T> iFillData;
    private List<Integer> selFlags = new ArrayList<>();

    public SimpleRvAdapter(@Nullable RecyclerView recyclerView, IFillData<T> iFillData) {
        super(recyclerView);
        this.iFillData = iFillData;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.simple_list_item);
    }

    private class Holder extends BaseViewHolder<T> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private CheckBox cbBox;
        private TextView tvName;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            cbBox = $(R.id.cbBox);
            tvName = $(R.id.tvName);
            itemView.setOnClickListener(this);
            cbBox.setClickable(false);
            cbBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void setData(T data, int position) {
            if (iFillData != null) {
                String name = iFillData.fillData(data);
                tvName.setText(name);
            }
            if (selFlags.contains(position)) {
                cbBox.setChecked(true);
                if (!isMulti) lastSelIndex = position;
            } else {
                cbBox.setChecked(false);
            }
        }

        @Override
        public void onClick(View v) {
            handleBox();
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }

        void handleBox() {
            boolean check = !cbBox.isChecked();
            cbBox.setChecked(check);
            if (!isMulti && check) {
                lastSelIndex = position;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isMulti) {
                if (lastSelIndex != position) {
                    Holder holder = (Holder) getViewHolderByPosition(lastSelIndex);
                    if (holder != null) {
                        CheckBox checkBox = holder.getView(R.id.cbBox);
                        checkBox.setChecked(false);
                    }
                }
            }
            if (isChecked) {
                if (!isMulti) selFlags.clear();
                if (!selFlags.contains(position)) {
                    selFlags.add(position);
                }
            } else {
                selFlags.remove((Integer) position);
            }
        }
    }

    public interface IFillData<T> {
        public String fillData(T t);
    }

    public List<T> getSelData() {
        List<T> res = new ArrayList<>();
        for (int index : selFlags) {
            res.add(dataList.get(index));
        }
        return res;
    }

    public SimpleRvAdapter setSelFlags(List<Integer> flags) {
        selFlags.addAll(flags);
        return this;
    }

    public SimpleRvAdapter setMulti(boolean isMulti) {
        this.isMulti = isMulti;
        return this;
    }

    public List<Integer> getSelFlags() {
        return selFlags;
    }

    public void clearSel() {
        selFlags.clear();
    }
}
