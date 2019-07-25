package com.lib.bandaid.widget.easyui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 该控件只显示list的第一张，剩下用来存储数据
 */
public class EventImageView extends AppCompatImageView {

    private String uuid;

    private List<String> list = new ArrayList<>();

    public EventImageView(Context context) {
        super(context);
    }

    public EventImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void addList(String path) {
        this.list.add(path);
    }
}
