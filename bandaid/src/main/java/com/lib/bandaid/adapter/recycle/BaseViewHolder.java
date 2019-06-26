package com.lib.bandaid.adapter.recycle;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zy on 2017/1/5.
 */

public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {
    protected int position;
    protected M data;

    /**
     * @param parent
     * @param resId  布局的id（R.layout.XXXXX）
     */
    public BaseViewHolder(ViewGroup parent, int viewType, @LayoutRes int resId, @LayoutRes int headId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }

    /**
     * @param parent
     * @param resId  布局的id（R.layout.XXXXX）
     */
    public BaseViewHolder(ViewGroup parent, @LayoutRes int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }

    public BaseViewHolder(Context context, @LayoutRes int resId) {
        super(LayoutInflater.from(context).inflate(resId, null, false));
    }

    public BaseViewHolder(View view) {
        super(view);
    }

    /**
     * 获取布局中的View
     *
     * @param viewId view的Id(R.id.XXXXX)
     * @param <T>    View的类型
     * @return view
     */
    protected <T extends View> T getView(@IdRes int viewId) {
        return (T) (itemView.findViewById(viewId));
    }

    protected <T extends View> T $(@IdRes int viewId) {
        return (T) (itemView.findViewById(viewId));
    }

    /**
     * 获取Context实例
     *
     * @return context
     */
    protected Context getContext() {
        return itemView.getContext();
    }

    /**
     * 设置数据
     *
     * @param data 要显示的数据对象
     */
    public abstract void setData(M data, int position);

    //public abstract void setEventListen(M data, int position);

    public int getPos() {
        return position;
    }

    public M getData() {
        return data;
    }
}
