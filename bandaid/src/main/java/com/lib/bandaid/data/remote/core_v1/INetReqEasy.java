package com.lib.bandaid.data.remote.core_v1;

import com.lib.bandaid.data.remote.core.INetRequest;
import com.lib.bandaid.data.remote.listen.NetWorkListen;

public interface INetReqEasy<T extends INetRequest.BaseView> {

    void detachView();

    <C, R> C request(Class<C> c, NetWorkListen<R> r);

    <C, R> C request(Class<C> c, NetWorkListen<R> r, boolean isShowLoading);

    public void cancelAllRequest();
}
