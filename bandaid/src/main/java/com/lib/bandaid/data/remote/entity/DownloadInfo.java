package com.lib.bandaid.data.remote.entity;

import com.lib.bandaid.data.remote.listen.DownWorkListen;
import com.lib.bandaid.rw.file.utils.FileUtil;

import java.io.File;

public class DownloadInfo {

    /**
     * 下载状态
     */
    public static final String DOWNLOAD_START = "start";
    public static final String DOWNLOAD = "download";
    public static final String DOWNLOAD_PAUSE = "pause";
    public static final String DOWNLOAD_CANCEL = "cancel";
    public static final String DOWNLOAD_OVER = "over";
    public static final String DOWNLOAD_ERROR = "error";
    public static final long TOTAL_ERROR = -1;//获取进度失败

    private String url;
    private String fileName;
    private String filePath;
    private String downloadStatus;
    private long total;
    private long progress;

    private DownWorkListen listen;

    public DownloadInfo(String url) {
        this.url = url;
    }

    public DownloadInfo(String url, String filePath) {
        this.url = url;
        this.filePath = filePath;
        this.fileName = new File(filePath).getName();
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        if (DOWNLOAD.equals(downloadStatus)
                && !DOWNLOAD.equals(this.downloadStatus)
                && !DOWNLOAD_START.equals(this.downloadStatus)
        ) {
            this.downloadStatus = DOWNLOAD_START;
        } else {
            this.downloadStatus = downloadStatus;
        }
    }

    public boolean isStart() {
        if (DOWNLOAD_START.equals(downloadStatus)) return true;
        return false;
    }

    public boolean isComplete() {
        if (DOWNLOAD_OVER.equals(downloadStatus)) return true;
        return false;
    }

    public boolean overButUnComplete() {
        return isCancel() || isError() || isPause();
    }

    public boolean isCancel() {
        if (DOWNLOAD_CANCEL.equals(downloadStatus)) return true;
        return false;
    }

    public boolean isDowning() {
        if (DOWNLOAD.equals(downloadStatus)) return true;
        return false;
    }

    public boolean isPause() {
        if (DOWNLOAD_PAUSE.equals(downloadStatus)) return true;
        return false;
    }

    public boolean isError() {
        if (DOWNLOAD_ERROR.equals(downloadStatus)) return true;
        return false;
    }

    public void setListen(DownWorkListen listen) {
        this.listen = listen;
    }

    public DownWorkListen getListen() {
        return this.listen;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", downloadStatus='" + downloadStatus + '\'' +
                ", total=" + total +
                ", progress=" + progress +
                '}';
    }
}

