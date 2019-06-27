package com.lib.bandaid.data.remote.core;


import android.app.Activity;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * @author zyp
 * 2019-05-09
 */
public interface INetRequest {
    public interface BasePresenter<T extends BaseView> {
        void attachView(T view);

        void detachView();

        <C, R> C request(Class<C> c, NetWorkListen<R> r);

        <C, R> C request(Class<C> c, NetWorkListen<R> r, boolean isShowLoading);

        public void cancelAllRequest();
    }

    public interface BaseView {
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
}
