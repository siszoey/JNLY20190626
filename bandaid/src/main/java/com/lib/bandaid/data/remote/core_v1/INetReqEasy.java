package com.lib.bandaid.data.remote.core_v1;

import android.app.Activity;

import com.lib.bandaid.data.remote.core.INetRequest;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.trello.rxlifecycle2.LifecycleTransformer;

public interface INetReqEasy<T extends ITipView> {

    void attachView(T view);

    void detachView();

    <C, R> C request(Class<C> c, NetWorkListen<R> r);

    <C, R> C request(Class<C> c, NetWorkListen<R> r, boolean isShowLoading);

    public void cancelAllRequest();
}
