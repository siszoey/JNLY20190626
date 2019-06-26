package com.lib.bandaid.widget.snackbar;

import android.graphics.Point;
import android.view.Display;


/**
 * Created by zy on 2018/9/10.
 */
class DisplayCompatImplPreHoneycombMR2 extends DisplayCompat.Impl {
    @Override
    void getSize(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }

    @Override
    void getRealSize(Display display, Point outSize) {
        outSize.x = display.getWidth();
        outSize.y = display.getHeight();
    }
}
