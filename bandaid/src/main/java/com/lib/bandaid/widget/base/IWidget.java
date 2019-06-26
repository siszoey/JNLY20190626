package com.lib.bandaid.widget.base;

/**
 * Created by zy on 2017/6/3.
 */

public interface IWidget {

    public void initialize();

    public void registerEvent();

    public void widgetReady();

    public void onDestroy();
}
