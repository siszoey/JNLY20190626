package com.lib.bandaid.widget.drag;

import android.support.v4.widget.ViewDragHelper;
import android.view.View;

/**
 * Created by zy on 2018/1/14.
 */

public class ViewMoveHolder {

    private View view;
    private ViewDragHelper viewDragHelper;

    public ViewMoveHolder() {

    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ViewDragHelper getViewDragHelper() {
        return viewDragHelper;
    }

    public void setViewDragHelper(ViewDragHelper viewDragHelper) {
        this.viewDragHelper = viewDragHelper;
    }

}
