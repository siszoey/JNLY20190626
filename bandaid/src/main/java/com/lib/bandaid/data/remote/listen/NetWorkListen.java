package com.lib.bandaid.data.remote.listen;

import io.reactivex.disposables.Disposable;

public interface NetWorkListen<R> {

    default void onStart(Disposable disposable) {

    }

    void onSuccess(R data);

    default void onError(int err, String errMsg, Throwable t) {

    }
}
