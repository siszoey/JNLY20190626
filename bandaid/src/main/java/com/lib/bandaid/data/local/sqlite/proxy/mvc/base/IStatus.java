package com.lib.bandaid.data.local.sqlite.proxy.mvc.base;


/**
 * Created by zy on 2017/6/25.
 * 几个重要的状态
 */

public interface IStatus<T> {
    /**
     *
     */
    public void onReady();

    /**
     * 结束
     */
    public void end();

    /**
     * 取消
     */
    public void cancel();

    /**
     * 销毁
     */
    public void onDestroy();
}
