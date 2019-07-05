package com.titan.jnly.system.version.bugly;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.tencent.bugly.crashreport.CrashReport;
import com.titan.jnly.Config;
import com.titan.jnly.R;

import java.io.File;

/**
 * bugly更新设置
 */
public class BuglySetting {

    public static void checkVersion() {
        Beta.checkUpgrade();
    }

    public static void init(Context context, String appId) {
        Beta.upgradeDialogLayoutId = R.layout.system_version_bugly_layout;
        Beta.autoCheckUpgrade = false;
        Beta.autoInit = true;
        Beta.initDelay = 500;
        Beta.canShowApkInfo = true;
        Beta.storageDir = new File(Config.APP_PATH);
        Beta.autoInstallApk = true;
        Beta.autoDownloadOnWifi = false;
        Beta.enableHotfix = true;
        Beta.enableNotification = false;
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask downloadTask) {
                LogUtils.iTag("更新：", downloadTask.getSavedLength() / downloadTask.getTotalLength());
            }

            @Override
            public void onCompleted(DownloadTask downloadTask) {
                LogUtils.iTag("下载完成：", 100);
            }

            @Override
            public void onFailed(DownloadTask downloadTask, int i, String s) {
                LogUtils.iTag("下载失败：", s);
            }
        });

        //企鹅的崩溃
        Bugly.init(context, appId, false);
        CrashReport.initCrashReport(context, appId, false);
    }
}
