package com.titan.jnly.common.fragment;

import android.content.Context;
import android.widget.Toast;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.data.remote.core.INetRequest;
import com.trello.rxlifecycle2.LifecycleTransformer;

public class BaseMvpFragment<T extends INetRequest.BasePresenter> extends BaseMainFragment implements INetRequest.BaseView {

    BaseMvpCompatAty attachActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseMvpCompatAty) {
            attachActivity = (BaseMvpCompatAty) context;
        } else {
            new Throwable("onAttach 必须继承为BaseMvpCompatAty");
        }
    }

    @Override
    public void showLoading() {
        attachActivity.showLoading();
    }

    @Override
    public void showLoading(String msg) {
        attachActivity.showLoading(msg);
    }

    @Override
    public void hideLoading() {
        attachActivity.hideLoading();
    }

    @Override
    public void showSuccess(String message) {
        attachActivity.showSuccess(message);
    }

    @Override
    public void showFail(String message) {
        attachActivity.showFail(message);
    }

    @Override
    public void showNoNet() {
        attachActivity.showNoNet();
    }

    @Override
    public void onRetry() {
        attachActivity.onRetry();
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return attachActivity.bindToLife();
    }

    public void showLongToast(Object info) {
        Toast.makeText(attachActivity, info + "", Toast.LENGTH_LONG).show();
    }
}
