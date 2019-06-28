package com.lib.bandaid.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.permission.Permission;
import com.lib.bandaid.permission.RxConsumer;
import com.lib.bandaid.permission.RxPermissionFactory;
import com.lib.bandaid.permission.SimplePermission;
import com.lib.bandaid.service.imp.LocService;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.utils.SystemUtil;

/**
 * Created by zy on 2019/5/10.
 * 权限和硬件的结合
 */

public final class PositionUtil {

    public final static int CODE = 0X999;

    private PositionUtil() {

    }

    public static boolean canLocUse(Context context) {
        return SystemUtil.isLocationOpen(context);
    }

    public static void reqGps(final Context context, final Class serClass) {
        //硬件设备已经开启支持定位的功能
        if (canLocUse(context)) {
            RxPermissionFactory.getRxPermissions(context).requestEachCombined(SimplePermission.MANIFEST_LOCATION).subscribe(new RxConsumer(context) {
                @Override
                public void accept(Permission permission) {
                    super.accept(permission);
                    if (permission.granted) {
                        context.startService(new Intent(context, serClass));
                    }
                }
            });
        }
        //打开定位设备
        else {
            openGPS(context, CODE);
        }
    }


    public static void openGPS(final Context context, final int requestCode) {
        new ATEDialog.Theme_Setting(context)
                .title("提示")
                .content("前去开启定位服务？")
                .positiveText("开启")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        if (context instanceof Activity) {
                            ((Activity) context).startActivityForResult(intent, requestCode);
                        }
                    }
                }).show();
    }

    public static void resGps(final Context context, int requestCode) {
        if (requestCode == CODE) {
            if (canLocUse(context)) {
                RxPermissionFactory.getRxPermissions(context).requestEachCombined(SimplePermission.MANIFEST_LOCATION).subscribe(new RxConsumer(context) {
                    @Override
                    public void accept(Permission permission) {
                        super.accept(permission);
                        if (permission.granted)
                            context.startService(new Intent(context, LocService.class));
                    }
                });
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * @return
     */
    public static boolean canUse() {
        return false;
    }

    public static void reqGps(final Context context, final Class serClass, @NonNull final ILocStatus iLocStatus) {
        //硬件设备已经开启支持定位的功能
        if (canLocUse(context)) {
            RxPermissionFactory.getRxPermissions(context).requestEachCombined(SimplePermission.MANIFEST_LOCATION).subscribe(new RxConsumer(context) {
                @Override
                public void accept(Permission permission) {
                    super.accept(permission);
                    if (permission.granted) {
                        context.startService(new Intent(context, serClass));
                        if (iLocStatus != null) iLocStatus.agree();
                    } else {
                        if (iLocStatus != null) iLocStatus.refuse();
                    }
                }
            });
        }
        //打开定位设备
        else {
            openGPS(context, CODE, iLocStatus);
        }
    }

    public static void openGPS(final Context context, final int requestCode, @NonNull final ILocStatus iLocStatus) {
        new ATEDialog.Theme_Setting(context)
                .title("提示")
                .content("前去开启定位服务？")
                .positiveText("开启")
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (iLocStatus != null) iLocStatus.refuse();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        if (context instanceof Activity) {
                            ((Activity) context).startActivityForResult(intent, requestCode);
                        }
                    }
                }).show();
    }

    public static void resGps(final Context context, int requestCode, final Class serClass, @NonNull final ILocStatus iLocStatus) {
        if (requestCode == CODE) {
            if (canLocUse(context)) {
                RxPermissionFactory.getRxPermissions(context).requestEachCombined(SimplePermission.MANIFEST_LOCATION).subscribe(new RxConsumer(context) {
                    @Override
                    public void accept(Permission permission) {
                        super.accept(permission);
                        if (permission.granted) {
                            context.startService(new Intent(context, serClass));
                            if (iLocStatus != null) iLocStatus.agree();
                        } else {
                            if (iLocStatus != null) iLocStatus.refuse();
                        }
                    }
                });
            } else {
                if (iLocStatus != null) iLocStatus.refuse();
            }
        }
    }


    public interface ILocStatus {
        public void agree();

        public void refuse();
    }
}
