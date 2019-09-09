package com.titan.jnly.common.fragment;

import android.content.Context;
import android.widget.Toast;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.activity.i.ITipView;
import com.lib.bandaid.data.remote.core.INetRequest;
import com.trello.rxlifecycle2.LifecycleTransformer;

public class BaseMvpFragment<T extends INetRequest.BasePresenter> extends BaseMainFragment implements ITipView {

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
    public void showContent() {
        attachActivity.showContent();
    }

    @Override
    public void showNoNetwork() {
        attachActivity.showNoNetwork();
    }

    @Override
    public void showEmpty() {
        attachActivity.showEmpty();
    }

    @Override
    public void showLoading() {
        attachActivity.showLoading();
    }

    @Override
    public void showError() {
        attachActivity.showError();
    }

    @Override
    public void dialogLoading() {
        attachActivity.dialogLoading();
    }

    @Override
    public void dialogHiding() {
        attachActivity.dialogHiding();
    }

    @Override
    public void showToast(Object o) {
        attachActivity.showToast(o);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return attachActivity.bindToLife();
    }

    public void showLongToast(Object info) {
        Toast.makeText(attachActivity, info + "", Toast.LENGTH_LONG).show();
    }
}
