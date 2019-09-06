package com.lib.bandaid.data.remote.core_v1;

import android.app.Activity;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface ITipView {
    //显示进度中
    void showLoading();

    void showLoading(String msg);

    //隐藏进度
    void hideLoading();

    //显示请求成功
    void showSuccess(String message);

    //失败重试
    void showFail(String message);

    //显示当前网络不可用
    void showNoNet();

    //重试
    void onRetry();

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

    Activity getActivity();
}
