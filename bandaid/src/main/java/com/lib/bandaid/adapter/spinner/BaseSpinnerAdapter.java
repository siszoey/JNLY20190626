package com.lib.bandaid.adapter.spinner;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.lib.bandaid.adapter.spinner.event.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/4/23.
 */

public abstract class BaseSpinnerAdapter<T>
        extends BaseAdapter
        implements AdapterView.OnItemSelectedListener {

    protected int resId;

    protected Context context;

    protected Spinner spinner;

    protected List<T> dataList = new ArrayList<>();

    protected ItemClickListener itemClickListener;

    public BaseSpinnerAdapter(Spinner spinner, @IdRes int resId) {
        this.resId = resId;
        this.spinner = spinner;
        if (this.spinner != null) this.spinner.setAdapter(this);
        this.context = spinner.getContext();
        this.spinner.setOnItemSelectedListener(this);

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(resId, parent, false);
        return bindData(convertView, dataList.get(position), position);
    }

    public abstract View bindData(View itemView, T data, int position);

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 移除
     *
     * @param position
     */
    public void removeItem(int position) {
        try {
            dataList.remove(position);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定数据
     *
     * @param model
     */
    public void removeItem(T model) {
        removeItem(dataList.indexOf(model));
    }

    public void insertItem(int position, T item) {
        try {
            dataList.add(position, item);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void removeAll() {
        try {
            dataList.clear();
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void replaceAll(List<T> items) {
        removeAll();
        appendList(items);
    }

    public void removeItems(List<T> items) {
        try {
            for (T m : items) {
                removeItem(dataList.indexOf(m));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void appendList(List<T> list) {
        try {
            dataList.addAll(list);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendList(List<T> list, boolean autoLoad) {
        try {
            dataList.addAll(list);
            if (autoLoad) {
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (itemClickListener != null)
            itemClickListener.itemSelected(view, dataList.get(position), position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
