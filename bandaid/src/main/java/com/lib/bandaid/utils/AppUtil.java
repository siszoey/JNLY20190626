package com.lib.bandaid.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2016/10/17.
 */

public class AppUtil {

    private AppUtil() {

    }

    /**
     * 检查包是否存在
     *
     * @param packName
     * @return
     */
    public static boolean hasPack(Context context, String packName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public static void startOtherAppActivity(Context context, Map<String, Object> params) {
        Intent intent = new Intent();
        try {
            String packageName = params.get("packageName").toString();
            String className = params.get("className").toString();
            intent.setClassName(packageName, className);
            Object val;
            for (String key : params.keySet()) {
                if (key.equals("packageName") || key.equals("className")) continue;
                val = params.get(key);
                if (val == null) continue;
                if (val instanceof String) {
                    intent.putExtra(key, (String) params.get(key));
                    continue;
                }
                if (val instanceof Integer) {
                    intent.putExtra(key, (Integer) params.get(key));
                    continue;
                }
                if (val instanceof Double) {
                    intent.putExtra(key, (Double) params.get(key));
                    continue;
                }
                if (val instanceof Float) {
                    intent.putExtra(key, (Float) params.get(key));
                    continue;
                }
                if (val instanceof Boolean) {
                    intent.putExtra(key, (Boolean) params.get(key));
                    continue;
                }
                if (val instanceof Long) {
                    intent.putExtra(key, (Long) params.get(key));
                    continue;
                }
                if (val instanceof Serializable) {
                    intent.putExtra(key, (Serializable) params.get(key));
                    continue;
                }
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startOtherAppActivityForResult(Context context, int requestCode, Map<String, Object> params) {
        Intent intent = new Intent();
        try {
            String packageName = params.get("packageName").toString();
            String className = params.get("className").toString();
            intent.setClassName(packageName, className);
            Object val;
            for (String key : params.keySet()) {
                if (key.equals("packageName") || key.equals("className")) continue;
                val = params.get(key);
                if (val == null) continue;
                if (val instanceof String) {
                    intent.putExtra(key, (String) params.get(key));
                    continue;
                }
                if (val instanceof Integer) {
                    intent.putExtra(key, (Integer) params.get(key));
                    continue;
                }
                if (val instanceof Double) {
                    intent.putExtra(key, (Double) params.get(key));
                    continue;
                }
                if (val instanceof Float) {
                    intent.putExtra(key, (Float) params.get(key));
                    continue;
                }
                if (val instanceof Boolean) {
                    intent.putExtra(key, (Boolean) params.get(key));
                    continue;
                }
                if (val instanceof Long) {
                    intent.putExtra(key, (Long) params.get(key));
                    continue;
                }
                if (val instanceof Serializable) {
                    intent.putExtra(key, (Serializable) params.get(key));
                    continue;
                }
            }
            ((Activity) context).startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息
     *
     * @param context
     * @return 当前应用的版本名称
     */

    public static String getApkVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getApkVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断进程是否运行
     *
     * @return
     */
    public static boolean isProcessRunning(Context context, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 判断手机是否可以定位。1：手机打开gps;2：手机连接到WiFi;3：手机连接到基站
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isLocationOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        //判断手机是否有连接到wifi
        boolean isWifiConnected = NetUtil.isWifiConnected(context);
        if (gps || network || isWifiConnected) {
            return true;
        }
        return false;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apkFile
     */
    public static void installApp(Context context, File apkFile) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String packName = getAppPackName(context);
            Uri apkFileUri = FileProvider.getUriForFile(context, packName + ".fileProvider", apkFile);
            installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
        } else {
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(installIntent);
    }


    public static String getAppPackName(Context context) {
        return context.getPackageName();
    }

    public static int SdkVersion() {
        return Build.VERSION.SDK_INT;
    }


    public static void getOtherApkInfo(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        try {
            String packageName = null;
            String path = null;
            for (PackageInfo info : packages) {
                packageName = info.packageName;
                PackageManager packageManager;
                if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    packageManager = context.getPackageManager();
                    path = packageManager.getApplicationInfo(packageName, 0).sourceDir;
                    StringBuilder sb = new StringBuilder();
                    sb.append("name=").append(info.applicationInfo.loadLabel(packageManager).toString()).append(";");
                    sb.append("packageName=").append(packageName).append(";");
                    sb.append("versionCode=").append(info.versionCode).append(";");
                    sb.append("versionName=").append(info.versionName).append(";");
                    sb.append("path=").append(path).append(";");
                    File file = new File(path);
                    if (file.exists()) {
                        long len = file.length();
                        sb.append("size=").append(len);
                    }
                    sb.append("\n");
                    System.out.println("app第三方应用：" + sb.toString());
                } else {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过包名获取此APP详细信息
     */
    public static PackageInfo getApkInfoWithPackageName(Context context, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static Integer getApkVersionWithPackageName(Context context, String packageName) {
        PackageInfo packageInfo = getApkInfoWithPackageName(context, packageName);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        } else {
            return null;
        }
    }


    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isApkInRelease(Context context) {
        return !isApkInDebug(context);
    }

    public static boolean hasActivity(Context context, Intent intent) {
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        }
        return true;
    }
}
