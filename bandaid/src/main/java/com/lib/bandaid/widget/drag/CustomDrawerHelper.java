package com.lib.bandaid.widget.drag;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * Created by zy on 2018/10/30.
 */

public final class CustomDrawerHelper {

    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float percentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            Field draggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            draggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) draggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * percentage)));
        } catch (Exception e) {
        }
    }

    public static void setDrawerRightEdgeSize(Activity activity, DrawerLayout drawerLayout, float percentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            Field draggerField = drawerLayout.getClass().getDeclaredField("mRightDragger");
            draggerField.setAccessible(true);
            ViewDragHelper rightDragger = (ViewDragHelper) draggerField.get(drawerLayout);
            Field edgeSizeField = rightDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(rightDragger);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(rightDragger, Math.max(edgeSize, (int) (dm.widthPixels * percentage)));
        } catch (Exception e) {
        }
    }

}
