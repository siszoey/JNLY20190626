package com.lib.bandaid.data.remote.core;


import android.app.Activity;

import com.lib.bandaid.activity.i.ITipView;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * @author zyp
 * 2019-05-09
 */
public interface INetRequest {
    public interface BasePresenter<T extends ITipView> {
        void attachView(T view);

        void detachView();

        <C, R> C request(Class<C> c, NetWorkListen<R> r);

        <C, R> C request(Class<C> c, boolean showLoading, NetWorkListen<R> r);

        public void cancelAllRequest();
    }


    public interface EasyPresenter {
        void attachView(ITipView view);

        void detachView();

        <C, R> C request(Class<C> c, NetWorkListen<R> r);

        <C, R> C request(Class<C> c, boolean showLoading, NetWorkListen<R> r);

        public void cancelAllRequest();
    }
}
