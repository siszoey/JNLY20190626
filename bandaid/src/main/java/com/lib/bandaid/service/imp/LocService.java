package com.lib.bandaid.service.imp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.lib.bandaid.service.com.baseservices.v2.BaseService;
import com.lib.bandaid.utils.AppCompatNotify;
import com.lib.bandaid.R;
import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by zy on 2018/3/30.
 * 定位服务 在整个app内广播位置
 */

public class LocService extends BaseService implements LocationListener, GpsStatus.Listener {

    /**
     * 定位时间间隔
     */
    private int interval = 5;
    /**
     *
     */
    private LocationManager locationManager;

    private Location cLocation = null;

    private ServiceLocation.ESignal eSignal;
    private ServiceLocation serviceLocation;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            listenLoc();
        }
    };

    @Override
    @SuppressLint("MissingPermission")
    protected void doInBackGroundingInitialize() {
        /**
         * 公交车 注册
         */
        isFrontService = true;
        serviceRunningInCreate = true;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.addGpsStatusListener(this);
        serviceLocation = new ServiceLocation();
        listenLoc();
        getLocation();
    }

    /**
     * 子线程
     */
    @Override
    protected void doInBackGrounding() {
        try {
            handler.obtainMessage().sendToTarget();
            getLocation();
            Thread.sleep(interval * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doCommandInBackGroundingInitialize() {

    }

    @Override
    protected void doCommandInBackGrounding() {

    }

    int notifyId = 0x1111;
    String channelId = "0x1111";
    String notifyTitle = "定位";
    String notifyContent = "运行中...";

    @Override
    protected void startNotify() {
        AppCompatNotify.create(this).notifyLocation(notifyId, channelId, notifyTitle, notifyContent, R.drawable.ic_location);
    }

    @Override
    protected void stopNotify() {
        if (Build.VERSION.SDK_INT >= 26) {
            AppCompatNotify.create(this).cancelNotify(channelId);
        } else {
            AppCompatNotify.create(this).cancelNotify(notifyId);
        }

    }

    //设置监听器，自动更新的最小时间为间隔15秒，最小位移变化超过5米
    private int minTime = 5;
    private int minDistance = 2;

    /**
     * 被动监听
     */
    @SuppressLint("MissingPermission")
    private void listenLoc() {
        System.out.println("被动监听");
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (cLocation != null) {
            synchronized (cLocation) {
                cLocation = location;
                System.out.println(cLocation.getLatitude() + "|" + cLocation.getLongitude());
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (LocationProvider.OUT_OF_SERVICE == status) {
            System.out.println("provider" + "GPS服务丢失,切换至网络定位");
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        System.out.println("位置服务打开 ！");
    }

    @Override
    public void onProviderDisabled(String s) {
        System.out.println("位置服务关闭 ！");
    }


    /**
     * 主动监听
     */
    @SuppressWarnings("MissingPermission")
    private void getLocation() {
        String provider = "unKnow";
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();
            if (cLocation != null) {
                synchronized (cLocation) {
                    if (cLocation != null) {
                        System.out.println("GPS定位成功！");
                        System.out.println(cLocation.getLatitude() + "|" + cLocation.getLongitude());
                    } else {
                        System.out.println("GPS定位失败！");
                    }
                }
            } else {
                cLocation = locationManager.getLastKnownLocation(provider);
            }
        }
        if (serviceLocation != null && ((serviceLocation.geteSignal() == null || ServiceLocation.ESignal.LOW == serviceLocation.geteSignal())
        )) {
            System.out.println("GPS信号弱！");
        }
        if (serviceLocation != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && (serviceLocation.geteSignal() == null || ServiceLocation.ESignal.LOW == serviceLocation.geteSignal())
                ) {
            provider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER).getName();
            if (cLocation != null) {
                synchronized (cLocation) {
                    if (cLocation != null) {
                        System.out.println("NET定位成功！");
                        System.out.println(cLocation.getLatitude() + "|" + cLocation.getLongitude());
                    } else {
                        System.out.println("NET定位失败！");
                    }
                }
            } else {
                cLocation = locationManager.getLastKnownLocation(provider);
            }
        }
        if (serviceLocation != null) {
            synchronized (serviceLocation) {
                serviceLocation.setLocation(cLocation);
            }
        }
        /**
         * 广播出去
         */
        if (serviceLocation.getLocation() != null) {
            EventBus.getDefault().post(serviceLocation);
            AppCompatNotify.create(this).notifyLocation(notifyId, channelId, notifyTitle, new Random().nextFloat() + "", R.drawable.ic_location);
        }
    }

    // GPS状态变化时的回调，如卫星数
    @Override
    public void onGpsStatusChanged(int event) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (event == GpsStatus.GPS_EVENT_STARTED) {
                //Log.d("zmenaGPS", "GPS event started ");
            }
            if (event == GpsStatus.GPS_EVENT_STOPPED) {
                //Log.d("zmenaGPS", "GPS event stopped ");
            }
            if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
                //Log.d("zmenaGPS", "GPS fixace ");
            }
            if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
                //Log.d("zmenaGPS", "GPS EVET NECO ");
                @SuppressLint("MissingPermission") GpsStatus status = locationManager.getGpsStatus(null); //取当前状态
                updateGpsStatus(event, status);
            }
        }
    }


    private void updateGpsStatus(int event, GpsStatus status) {
        if (status == null) return;
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            int count = 0;
            int valid = 0;
            GpsSatellite s;
            while (it.hasNext() && count <= maxSatellites) {
                s = it.next();
                count++;
                if (s.getSnr() > 30) {
                    valid++;
                }
            }
            if (serviceLocation == null) {
                serviceLocation.seteSignal(ServiceLocation.ESignal.LOW);
                return;
            }
            synchronized (serviceLocation) {
                if (valid >= 6) {
                    //表示有信号
                    eSignal = ServiceLocation.ESignal.HIGH;
                }
                if (valid >= 4 && valid < 6) {
                    //表示有信号
                    eSignal = ServiceLocation.ESignal.MIDDLE;
                }
                if (valid < 4) {
                    //信号弱或无信号
                    eSignal = ServiceLocation.ESignal.LOW;
                }
                serviceLocation.seteSignal(eSignal);
                serviceLocation.setSatelliteCount(count);
                serviceLocation.setValidSatelliteCount(valid);
            }
        }
    }

    public ServiceLocation getServiceLocation() {
        return serviceLocation;
    }

}
