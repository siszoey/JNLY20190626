package com.lib.bandaid.data.remote.core_v1;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.data.remote.core.INetRequest;
import com.lib.bandaid.data.remote.core.NetRequest;
import com.lib.bandaid.data.remote.entity.BaseResult;
import com.lib.bandaid.data.remote.entity.TTResult;
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
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class NetReqEasy<T extends INetRequest.BaseView> implements INetReqEasy<T> {

    protected T view;
    private ConcurrentHashMap<Observable, Disposable> networkMap;

    public static NetReqEasy create(INetRequest.BaseView view) {
        return new NetReqEasy().attachView(view);
    }

    public static NetReqEasy create(Context context) {
        if (context instanceof BaseMvpCompatAty) {
            return new NetReqEasy().attachView((BaseMvpCompatAty) context);
        } else {
            new Throwable("context not instanceof BaseMvpCompatAty!");
            return null;
        }
    }


    private NetReqEasy() {
        networkMap = new ConcurrentHashMap<>();
    }

    private NetReqEasy attachView(T view) {
        this.view = view;
        return this;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public <C, R> C request(Class<C> c, NetWorkListen<R> r) {
        return request(c, r, true);
    }

    @Override
    public <C, R> C request(Class<C> c, NetWorkListen<R> r, boolean isShowLoading) {
        if (isShowLoading && view != null) view.showLoading();
        C IApi = RetrofitManager.create(c);
        Object o = Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, new NetReqEasy.InvokeRequestHandler(IApi, r));
        return (C) o;
    }

    private final class InvokeRequestHandler<R> implements InvocationHandler {
        private Object obj;
        private NetWorkListen<R> netWorkResult;

        public InvokeRequestHandler(Object o, NetWorkListen<R> netWorkResult) {
            obj = o;
            this.netWorkResult = netWorkResult;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object invoke = method.invoke(obj, args);
            if (invoke instanceof Observable) {
                Observable<R> observable = (Observable) invoke;
                Disposable disposable = observable.compose(view.bindToLife())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(dataResponse -> {
                            networkMap.remove(observable);
                            if (dataResponse instanceof BaseResult) {
                                BaseResult result = (BaseResult) dataResponse;
                                if (result.getCode() == 200 || result.getCode() == 0) {
                                    netWorkResult.onSuccess(dataResponse);
                                } else {
                                    ToastUtils.showShort("响应码:" + result.getCode() + " 信息:" + result.getMsg());
                                    LogUtils.eTag("NetworkError", result.getCode() + "---" + result.getMsg());
                                    netWorkResult.onError(result.getCode(), result.getMsg(), null);
                                }
                            } else if (dataResponse instanceof TTResult) {
                                TTResult result = (TTResult) dataResponse;
                                if (result.getResult()) {
                                    netWorkResult.onSuccess(dataResponse);
                                } else {
                                    ToastUtils.showShort(" 信息:" + result.getMessage());
                                    LogUtils.eTag("NetworkError", result.getMessage());
                                    netWorkResult.onError(result.getCode(), result.getMessage(), null);
                                }
                            } else if (dataResponse instanceof Response) {

                            } else {
                                netWorkResult.onSuccess(dataResponse);
                            }
                            if (networkMap.size() == 0) {
                                if (view != null) view.hideLoading();
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
                                if (netWorkResult != null) {
                                    netWorkResult.onError(httpException.code(), throwable.getMessage(), throwable);
                                }
                            } else if (throwable instanceof SocketTimeoutException) {
                                ToastUtils.showShort("加载失败:" + "响应超时");
                                LogUtils.eTag("NetworkError", "响应超时:" + value);
                                if (netWorkResult != null) {
                                    netWorkResult.onError(-2, throwable.getMessage(), throwable);
                                }
                            } else if (throwable instanceof UnknownHostException) {
                                UnknownHostException unknownHostException = (UnknownHostException) throwable;
                                ToastUtils.showShort("未知主机:" + unknownHostException.getMessage());
                                LogUtils.eTag("NetworkError", "未知主机:" + unknownHostException.getMessage() + "----" + value);
                            } else {
                                ToastUtils.showShort("加载失败:" + throwable.getMessage());
                                LogUtils.eTag("NetworkError", throwable.getMessage());
                                if (netWorkResult != null) {
                                    netWorkResult.onError(-1, throwable.getMessage(), throwable);
                                }
                            }
                            throwable.printStackTrace();
                            if (networkMap.size() == 0) {
                                if (view != null) view.hideLoading();
                            }
                        });
                networkMap.put(observable, disposable);
                netWorkResult.onStart(disposable);
            }
            return invoke;
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
