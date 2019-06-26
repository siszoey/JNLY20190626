package com.lib.bandaid.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.lib.bandaid.R;


/**
 * Created by zy on 2019/4/30.
 */

public final class AppCompatNotify extends ContextWrapper {

    public static final String id = "channel_1";
    //public static final String name = "channel_name_1";

    Context context;
    static NotificationManager manager;
    static AppCompatNotify compatNotify;

    private AppCompatNotify(Context context) {
        super(context);
        this.context = context;
    }

    public static AppCompatNotify create(Context context) {
        if (manager == null)
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        return new AppCompatNotify(context);
    }

    public static NotificationManager getManager() {
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void initNotification26() {
        NotificationChannel channel = new NotificationChannel(id, id + "_name", NotificationManager.IMPORTANCE_DEFAULT);
        if (manager != null) manager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void initNotification26(String id) {
        NotificationChannel channel = new NotificationChannel(id, id + "_name", NotificationManager.IMPORTANCE_DEFAULT);
        if (manager != null) manager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder createNotification26(String title, String content) {
        return new Notification.Builder(this, id)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
    }

    public Notification.Builder createNotificationBelow26(String title, String content) {
        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder createNotification26(String title, String content, @DrawableRes int smallIcon) {
        return new Notification.Builder(this, id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true);
    }

    public Notification.Builder createNotificationBelow26(String title, String content, @DrawableRes int smallIcon) {
        return new Notification.Builder(this)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder createNotification26(String title, String content, @DrawableRes int smallIcon, @DrawableRes int largeIcon) {
        return new Notification.Builder(this, id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                )
                .setWhen(System.currentTimeMillis())
                .setShowWhen(false)
                .setAutoCancel(true);
    }

    public Notification.Builder createNotificationBelow26(String title, String content, @DrawableRes int smallIcon, @DrawableRes int largeIcon) {
        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                )
                .setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder createNotification26(String ticker, String title, String content, @DrawableRes int smallIcon, @DrawableRes int largeIcon) {
        return new Notification.Builder(this, id)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker)
                .setSmallIcon(smallIcon)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                )
                .setWhen(System.currentTimeMillis())
                .setShowWhen(false)
                .setAutoCancel(true);
    }

    public Notification.Builder createNotificationBelow26(String ticker, String title, String content, @DrawableRes int smallIcon, @DrawableRes int largeIcon) {
        return new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker)
                .setSmallIcon(smallIcon)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                )
                .setAutoCancel(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifySimple(int id, String title, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            initNotification26();
            Notification notification = createNotification26(title, content).build();
            if (manager != null) manager.notify(id, notification);
        } else {
            Notification notification = createNotificationBelow26(title, content).build();
            if (manager != null) manager.notify(id, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifySimple(int id, String title, String content, @DrawableRes int smallIcon) {
        if (Build.VERSION.SDK_INT >= 26) {
            initNotification26();
            Notification notification = createNotification26(title, content, smallIcon).build();
            if (manager != null) manager.notify(id, notification);
        } else {
            Notification notification = createNotificationBelow26(title, content, smallIcon).build();
            if (manager != null) manager.notify(id, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifySimple(int id, String title, String content, @DrawableRes int smallIcon, @DrawableRes int largeIcon) {
        if (Build.VERSION.SDK_INT >= 26) {
            initNotification26();
            Notification notification = createNotification26(title, content, smallIcon, largeIcon).build();
            notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
            if (manager != null) manager.notify(id, notification);
        } else {
            Notification notification = createNotificationBelow26(title, content, smallIcon, largeIcon).build();
            if (manager != null) manager.notify(id, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotifySimple(int id, String ticker, String title, String content, @DrawableRes int smallIcon, @DrawableRes int largeIcon) {
        if (Build.VERSION.SDK_INT >= 26) {
            initNotification26();
            Notification notification = createNotification26(ticker, title, content, smallIcon, largeIcon).build();
            notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
            if (manager != null) manager.notify(id, notification);
        } else {
            Notification notification = createNotificationBelow26(ticker, title, content, smallIcon, largeIcon).build();
            if (manager != null) manager.notify(id, notification);
        }
    }

    public void cancelNotify(Object id) {
        try {
            if (id == null) return;
            if (Build.VERSION.SDK_INT >= 26) {
                if (manager != null) manager.deleteNotificationChannel(id.toString());
            } else {
                if (manager != null) manager.cancel((int) id);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    //定制的定位服务
    public void notifyLocation(int id, String channelId, String title, String content, @DrawableRes int smallIcon) {
        Notification notification;
        RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.system_notify_normal_layout);
        customView.setImageViewResource(R.id.ivLoc, R.mipmap.ic_launcher);
        customView.setTextViewText(R.id.tvNotifyTitle, title);
        customView.setTextColor(R.id.tvNotifyTitle, Color.BLACK);
        customView.setTextViewText(R.id.tvNotifySub, content);
        customView.setTextColor(R.id.tvNotifySub, Color.BLACK);
        customView.setTextViewText(R.id.tvNotifyTime, DateUtil.getCurrentTime("HH:mm"));
        customView.setTextColor(R.id.tvNotifyTime, Color.BLACK);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId, channelId + "_name", NotificationManager.IMPORTANCE_LOW);
            channel.setVibrationPattern(new long[]{0});
            if (manager != null) manager.createNotificationChannel(channel);
            notification = new Notification.Builder(this, channelId)
                    .setCustomContentView(customView)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(smallIcon)
                    .setLargeIcon(
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                    )
                    .setShowWhen(false)
                    .setAutoCancel(true).build();
            notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
        } else {
            notification = new Notification.Builder(this)
                    .setContent(customView)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(smallIcon)
                    .setVibrate(new long[]{0})
                    .setLargeIcon(
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                    )
                    .setAutoCancel(true).build();
        }
        if (manager != null && notification != null) manager.notify(id, notification);
    }
}
