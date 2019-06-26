package com.lib.bandaid.widget.snackbar;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;


/**
 * Created by zy on 2018/9/10.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
class DisplayCompatImplHoneycombMR2 extends DisplayCompat.Impl {
    @Override
    void getSize(Display display, Point outSize) {
        display.getSize(outSize);
    }

    @Override
    void getRealSize(Display display, Point outSize) {
        display.getSize(outSize);
    }
}
