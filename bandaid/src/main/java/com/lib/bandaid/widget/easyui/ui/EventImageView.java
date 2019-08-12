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

    private String json;

    private List<String> list = new ArrayList<>();

    public EventImageView(Context context) {
        super(context);
        init();

    }

    public EventImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void init(){
        //setScaleType(ScaleType.FIT_CENTER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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
