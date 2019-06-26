package com.lib.bandaid.permission;

import android.content.Context;

import io.reactivex.functions.Consumer;

/**
 * Created by zy on 2019/4/11.
 */

public class RxConsumer implements Consumer<Permission> {

    private Context context;
    String[] mPermissions;

    public RxConsumer() {
    }

    public RxConsumer(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void accept(Permission permission) {
        if (permission.isAllFail()) {//权限获取失败，而且被永久拒绝
            mPermissions = PermissionUtil.getDeniedPermissions(context, permission.names);
            PermissionUtil.PermissionDialog(context, PermissionUtil.permissionText(mPermissions) + "请在应用权限管理进行设置！");
        }
        if (permission.shouldRequestAgain) { //权限获取失败，但是没有永久拒绝

        }
        if (permission.granted) {//权限获取成功

        }
    }
}
