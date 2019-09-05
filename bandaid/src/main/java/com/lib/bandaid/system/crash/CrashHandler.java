package com.lib.bandaid.system.crash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.lib.bandaid.app.ActivityLifeCycle;
import com.lib.bandaid.app.BaseApp;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.data.local.sqlite.utils.UUIDTool;
import com.lib.bandaid.system.crash.bean.CrashInfo;
import com.lib.bandaid.system.crash.ui.CrashActivity;
import com.lib.bandaid.system.crash.utils.DateUtil;
import com.lib.bandaid.system.crash.utils.FileUtil;
import com.lib.bandaid.util.AppUtil;
import com.lib.bandaid.util.SPfUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


/**
 * Created by zy on 2017/12/11.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static String TAG = "MyCrash";
    private static CrashHandler instance = new CrashHandler();

    private String crashParentPath;
    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;

    // 用来存储设备信息和异常信息
    private Map<String, String> info = new HashMap<>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private String url;
    private Observable observable;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context, String crashParentPath) {
        this.context = context;
        this.crashParentPath = crashParentPath;
        File file = new File(crashParentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        // 获取系统默认的UncaughtException处理器
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        autoClear(5);
    }

    public CrashHandler init(Context context) {
        this.context = context;
        // 获取系统默认的UncaughtException处理器
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        autoClear(5);
        return this;
    }

    /**
     * 本地文件存储
     *
     * @param crashParentPath
     * @return
     */
    public CrashHandler localFile(String crashParentPath) {
        this.crashParentPath = crashParentPath;
        File file = new File(crashParentPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return this;
    }


    public CrashHandler reportUrl(String url) {
        this.url = url;
        return this;
    }

    public Observable getObservable() {
        return observable;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex);
        } else {
            SystemClock.sleep(2000);
            BaseApp baseApplication = BaseApp.baseApp;
            if (baseApplication != null) {
                ActivityLifeCycle atyLifecycleCallback = baseApplication.getAtyLifecycleCallback();
                if (atyLifecycleCallback != null) atyLifecycleCallback.removeAllActivities();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) return false;
        try {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(context, "很抱歉,程序出现异常,正在尝试收集错误信息！", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
            // 收集设备参数信息
            collectDeviceInfo(context);
            // 保存日志文件
            saveCrashInfoFile(ex);
            //SystemClock.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     * @throws
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new Date());
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : info.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            String fileName = writeFile(sb.toString());
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
            writeFile(sb.toString());
        }
        return null;
    }

    private String writeFile(String sb) throws Exception {
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + ".log";
        if (FileUtil.hasSdcard()) {
            String path = crashParentPath;
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            FileOutputStream fos = new FileOutputStream(path + File.separator + fileName, true);
            fos.write(sb.getBytes());
            fos.flush();
            fos.close();
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();


                DbManager.createDefault().createIfNecessary(CrashInfo.class);
                CrashInfo crashInfo = new CrashInfo();
                crashInfo.setUuid(UUIDTool.get32UUID());
                crashInfo.setAppKey(AppUtil.getAppPackName(context));
                crashInfo.setAppName(AppUtil.getAppName(context));
                crashInfo.setVersionCode(AppUtil.getApkVersionCode(context));
                crashInfo.setVersionName(AppUtil.getApkVersionName(context));
                crashInfo.setCrashInfo(sb);
                crashInfo.setCrashTime(new Date());
                crashInfo.reportUrl = url;
                DbManager.createDefault().save(crashInfo);


                Boolean reportError = SPfUtil.readT(context, "REPORT_ERROR");
                if (reportError == null) {
                    Intent intent = new Intent(context, CrashActivity.class);
                    intent.putExtra(TAG, crashInfo);
                    context.startActivity(intent);
                } else if (reportError) {
                    //submitError(crashInfo, null);
                }
                Looper.loop();
            }
        }.start();

        return fileName;
    }

    /**
     * 文件删除
     *
     * @param autoClearDay 文件保存天数
     */
    public void autoClear(final int autoClearDay) {
        FileUtil.delete(crashParentPath, new FilenameFilter() {

            @Override
            public boolean accept(File file, String filename) {
                String s = FileUtil.getFileNameWithoutExtension(filename);
                int day = autoClearDay < 0 ? autoClearDay : -1 * autoClearDay;
                String date = "crash-" + DateUtil.getOtherDay(day);
                return date.compareTo(s) >= 0;
            }
        });
    }

    /*private void submitError(CrashInfo crashInfo, IMvpHttpListener iMvpHttpListener) {
        observable = BaseApi.createDefaultApi(IHttpCrash.class).httpCrash(url, crashInfo);
        if (observable == null) return;
        BaseApi.doRequest(observable, iMvpHttpListener);
    }*/

}

