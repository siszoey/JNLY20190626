package com.lib.bandaid.adapter.recycle.simple;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;

/**
 * Created by zy on 2018/9/25.
 */

public class SimpleRecycleAdapter<T> extends BaseRecycleAdapter<T, BaseViewHolder<T>> {

    private IFillData<T> iFillData;

    public SimpleRecycleAdapter(@Nullable RecyclerView recyclerView, IFillData<T> iFillData) {
        super(recyclerView);
        this.iFillData = iFillData;
    }

    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, android.R.layout.simple_list_item_1);
    }

    @Override
    public void isPosItemVisible(int firstItemPosition, int lastItemPosition) {

    }

    @Override
    public void scrollState(RecyclerView recyclerView, int newState) {

    }

    private class Holder extends BaseViewHolder<T> implements View.OnClickListener {
        private TextView tvName;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            tvName = $(android.R.id.text1);
            tvName.setOnClickListener(this);
        }

        @Override
        public void setData(T data, int position) {
            if (iFillData != null) {
                String name = iFillData.fillData(data);
                tvName.setText(name);
            }
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }
    }

    public interface IFillData<T> {
        public String fillData(T t);
    }

}