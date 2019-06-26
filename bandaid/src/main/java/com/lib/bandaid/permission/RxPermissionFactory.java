package com.lib.bandaid.permission;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zy on 2019/4/11.
 */

public final class RxPermissionFactory {

    public static RxPermissions getRxPermissions(Context context) {
        return new RxPermissions(context);
    }

    public static void askMultiPermission(Context context, RxConsumer rxConsumer, String[]... permissions) {
        if (permissions == null || permissions.length == 0) return;
        Set<String> set = new HashSet<>();
        int j;
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i] == null || permissions[i].length == 0) continue;
            for (j = 0; j < permissions[i].length; j++) {
                set.add(permissions[i][j]);
            }
        }
        String[] permission = new String[set.size()];
        set.toArray(permission);
        rxConsumer.setContext(context);
        new RxPermissions(context).requestEachCombined(permission).subscribe(rxConsumer);
    }

}
