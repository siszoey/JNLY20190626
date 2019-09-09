package com.lib.bandaid.data.remote.com;

import android.content.Context;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.activity.i.ITipView;
import com.lib.bandaid.data.remote.core.NetRequest;

public final class NetEasyFactory {

    public static<T extends ITipView> NetRequest create(T view) {
        NetRequest<T> netRequest = new NetRequest();
        netRequest.attachView(view);
        return netRequest;
    }

    public static NetRequest create(Context context) {
        if (context instanceof BaseMvpCompatAty) {
            ITipView view = (BaseMvpCompatAty) context;
            return create(view);
        } else {
            new Throwable("context not instanceof ViewRxThemeActivity!");
            return null;
        }
    }

    public static NetRequest create(BaseMvpCompatAty activity) {
        ITipView view = activity;
        return create(view);
    }

    public static <T> T convert(Object o) {
        return (T) o;
    }


    public static<T extends ITipView> NetEasyReq createEasy(T view) {
        NetEasyReq netRequest = new NetEasyReq();
        netRequest.attachView(view);
        return netRequest;
    }

    public static NetEasyReq createEasy(Context context) {
        if (context instanceof BaseMvpCompatAty) {
            ITipView view = (BaseMvpCompatAty) context;
            return createEasy(view);
        } else {
            new Throwable("context not instanceof ViewRxThemeActivity!");
            return null;
        }
    }

    public static NetEasyReq createEasy(BaseMvpCompatAty activity) {
        ITipView view = activity;
        return createEasy(view);
    }
}
