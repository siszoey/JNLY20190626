package com.lib.bandaid.widget.dialog.i;

import com.lib.bandaid.utils.DialogFactory;

public interface IView {

    public void showContent();

    public void showNoNetwork();

    public void showEmpty();

    public void showLoading();

    public void showError();


    public void dialogLoading();

    public void dialogHiding();

}
