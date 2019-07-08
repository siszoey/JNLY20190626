package com.titan.jnly.vector.inter;

import com.lib.bandaid.arcruntime.core.draw.ValueCallback;

public interface ValueBack extends ValueCallback {

    void onSuccess(Object o);

    void onFail(String info);

}
