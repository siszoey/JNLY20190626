package com.lib.bandaid.activity.i;

import android.app.Activity;

import com.trello.rxlifecycle2.LifecycleTransformer;

public interface ITipView {
    public void showContent();

    public void showNoNetwork();

    public void showEmpty();

    public void showLoading();

    public void showError();

    public void dialogLoading();

    public void dialogHiding();

    public void showToast(Object o);

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

    Activity getActivity();
}
