package com.lib.bandaid.widget.other;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zy on 2017/6/29.
 */

public class ComLinerLayout extends LinearLayout {

    private boolean childEventIntercept = false;

    public ComLinerLayout(Context context) {
        super(context);
    }

    public ComLinerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ComLinerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写这个方法，返回true就行了
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return childEventIntercept;
    }


    public boolean isChildEventIntercept() {
        return childEventIntercept;
    }

    public void setChildEventIntercept(boolean childEventIntercept) {
        this.childEventIntercept = childEventIntercept;
    }
}
