package com.lib.bandaid.data.remote.core;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.bandaid.data.remote.entity.BaseResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.data.remote.utils.RetrofitManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class NetRequest<T extends INetRequest.BaseView> implements INetRequest.BasePresenter<T> {

    protected T mView;
    private ConcurrentHashMap<Observable, Disposable> networkMap;

    public NetRequest() {
        networkMap = new ConcurrentHashMap<>();
    }


    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    /*@Override
    public void detachView(BaseActivity baseActivity) {

    }*/

    @Override
    public <C, R> C request(Class<C> c, NetWorkListen<R> r) {
        return request(c, r, true);
    }

    @Override
    public <C, R> C request(Class<C> c, NetWorkListen<R> r, boolean isShowLoading) {
        if (isShowLoading && mView != null) mView.showLoading();
        C IApi = RetrofitManager.create(c);
        Object o = Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, new InvokeRequestHandler<R>(IApi, r));
        return (C) o;
    }

    private final class InvokeRequestHandler<R> implements InvocationHandler {
        private Object obj;
        private NetWorkListen<R> mNetWorkResult;

        public InvokeRequestHandler(Object o, NetWorkListen<R> netWorkResult) {
            obj = o;
            mNetWorkResult = netWorkResult;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object invoke = method.invoke(obj, args);
            if (invoke instanceof Observable) {
                Observable<R> observable = (Observable) invoke;
                Disposable disposable = observable.compose(mView.bindToLife())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(dataResponse -> {
                            networkMap.remove(observable);
                            if (dataResponse instanceof BaseResult) {
                                if (((BaseResult) dataResponse).getCode() == 0) {
                                    mNetWorkResult.onSuccess(dataResponse);
                                } else {
                                    ToastUtils.showShort("加载失败:服务器响应码:" + ((BaseResult) dataResponse).getCode());
                                    LogUtils.eTag("NetworkError", ((BaseResult) dataResponse).getCode() + "---" + ((BaseResult) dataResponse).getMsg());
                                    mNetWorkResult.onError(((BaseResult) dataResponse).getCode(), ((BaseResult) dataResponse).getMsg(), null);
                                }
                            } else {
                                mNetWorkResult.onSuccess(dataResponse);
                            }
                            if (networkMap.size() == 0) {
                                if (mView != null) mView.hideLoading();
                            }
                        }, throwable -> {
                            networkMap.remove(observable);
                            POST postAnnotation = method.getAnnotation(POST.class);
                            GET getAnnotation = method.getAnnotation(GET.class);
                            DELETE deleteAnnotation = method.getAnnotation(DELETE.class);
                            String value = "";
                            if (postAnnotation != null) {
                                value = "post:" + postAnnotation.value();
                            } else if (getAnnotation != null) {
                                value = "get:" + getAnnotation.value();
                            } else if (deleteAnnotation != null) {
                                value = "delete:" + deleteAnnotation.value();
                            }
                            if (throwable instanceof HttpException) {
                                HttpException httpException = (HttpException) throwable;
                                ToastUtils.showShort("加载失败:" + httpException.getMessage());
                                LogUtils.eTag("NetworkError", httpException.response().raw().toString());
                                if (mNetWorkResult != null) {
                                    mNetWorkResult.onError(httpException.code(), throwable.getMessage(), throwable);
                                }
                            } else if (throwable instanceof SocketTimeoutException) {
                                //SocketTimeoutException socketTimeoutException = (SocketTimeoutException) throwable;
                                ToastUtils.showShort("加载失败:" + "响应超时");
                                LogUtils.eTag("NetworkError", "响应超时:" + value);
                                if (mNetWorkResult != null) {
                                    mNetWorkResult.onError(-2, throwable.getMessage(), throwable);
                                }
                            } else if (throwable instanceof UnknownHostException) {
                                UnknownHostException unknownHostException = (UnknownHostException) throwable;
                                ToastUtils.showShort("未知主机:" + unknownHostException.getMessage());
                                LogUtils.eTag("NetworkError", "未知主机:" +
                                        unknownHostException.getMessage() + "----" + value);
                            } else {
                                ToastUtils.showShort("加载失败:" + throwable.getMessage());
                                LogUtils.eTag("NetworkError", throwable.getMessage());
                                if (mNetWorkResult != null) {
                                    mNetWorkResult.onError(-1, throwable.getMessage(), throwable);
                                }
                            }
                            throwable.printStackTrace();
                            if (networkMap.size() == 0) {
                                if (mView != null) mView.hideLoading();
                            }
                        });
                networkMap.put(observable, disposable);
                mNetWorkResult.onStart(disposable);
            }
            return invoke;
        }

        private void loadError(Throwable throwable) {
            if (mView != null)mView.hideLoading();
            ToastUtils.showShort("加载失败");
            if (mNetWorkResult != null) {
                mNetWorkResult.onError(-1, throwable.getMessage(), throwable);
            }
            throwable.printStackTrace();
        }
    }

    public void cancelAllRequest() {
        for (Map.Entry<Observable, Disposable> observableDisposableEntry : networkMap.entrySet()) {
            Disposable value = observableDisposableEntry.getValue();
            if (!value.isDisposed()) {
                value.dispose();
            }
        }
    }

    public ConcurrentHashMap<Observable, Disposable> getNetworkMap() {
        return networkMap;
    }

    protected boolean isHaveNetworkRequest() {
        return networkMap.size() != 0;
    }

}
