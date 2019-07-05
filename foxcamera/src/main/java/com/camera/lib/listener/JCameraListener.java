package com.camera.lib.listener;

import android.graphics.Bitmap;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/4/26
 * 描    述：
 * =====================================
 */
public interface JCameraListener {
    /**
     * 拍照回调
     * @param bitmap
     */
    void captureSuccess(Bitmap bitmap);

    /**
     * 小视频回调
     * @param url
     * @param firstFrame
     */
    void recordSuccess(String url, Bitmap firstFrame);

}
