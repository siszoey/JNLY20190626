package com.lib.bandaid.data.remote.function;

import android.util.Log;

import com.lib.bandaid.data.remote.core.DownloadManager;
import com.lib.bandaid.data.remote.entity.DownloadInfo;
import com.lib.bandaid.data.remote.listen.DownWorkListen;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DownloadObserver implements Observer<DownloadInfo> {

    public Disposable disposable;//可以用于取消注册的监听者
    public DownloadInfo downloadInfo;
    private DownWorkListen listen;

    public DownloadObserver() {
    }

    public DownloadObserver(DownWorkListen listen) {
        this.listen = listen;
    }

    public void setListen(DownWorkListen listen) {
        this.listen = listen;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onNext(DownloadInfo value) {
        this.downloadInfo = value;
        downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD);
        if (downloadInfo.getListen() != null) downloadInfo.getListen().progress(downloadInfo);
        EventBus.getDefault().post(downloadInfo);
    }

    @Override
    public void onError(Throwable e) {
        Log.d("My_Log", "onError");
        if (DownloadManager.getInstance().getDownloadUrl(downloadInfo.getUrl())) {
            DownloadManager.getInstance().pauseDownload(downloadInfo.getUrl());
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_ERROR);
            if (downloadInfo.getListen() != null) downloadInfo.getListen().progress(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        } else {
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_PAUSE);
            if (downloadInfo.getListen() != null) downloadInfo.getListen().progress(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        }

    }

    @Override
    public void onComplete() {
        Log.d("My_Log", "onComplete");
        if (downloadInfo != null) {
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_OVER);
            if (downloadInfo.getListen() != null) downloadInfo.getListen().progress(downloadInfo);
            EventBus.getDefault().post(downloadInfo);
        }
    }
}

