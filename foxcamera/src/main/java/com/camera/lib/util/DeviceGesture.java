package com.camera.lib.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zy on 2019/6/13.
 * 设备姿态
 */

public final class DeviceGesture implements SensorEventListener {

    private static DeviceGesture singleton;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);


    public static synchronized DeviceGesture getInstance(Context context) {
        if (singleton == null) {
            singleton = new DeviceGesture();
            if (atomicInteger.get() == 0) {
                singleton.onCreate(context);
            }
            atomicInteger.incrementAndGet();
        }
        return singleton;
    }


    private DeviceGesture() {
    }


    /**
     * 计算手机姿态
     */
    private SensorManager mSensorManager;
    /**
     * 加速度传感器
     */
    private Sensor accelerometer;
    /**
     * 地磁场传感器
     */
    private Sensor magnetic;

    private double azimuth;
    private double pitch;
    private double roll;
    private boolean listenDirection = true;
    String headOrientation = "未知方向";
    private String cameraOrientation = "未知方向";
    private String faceOrientation = "未知方向";
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private IOrientChange iOrientChange;

    private void onCreate(Context context) {
        // 实例化传感器管理者
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // 初始化加速度传感器
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 初始化地磁场传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // 注册监听
        mSensorManager.registerListener(this, accelerometer, Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, magnetic, Sensor.TYPE_MAGNETIC_FIELD);
    }

    public synchronized void onDestroy() {
        int count = atomicInteger.decrementAndGet();
        if (count <= 0) {
            mSensorManager.unregisterListener(this);
        }
    }

    public void forceDestroy() {
        atomicInteger = null;
        mSensorManager.unregisterListener(this);
    }

    private DeviceGesture computePhoneGesture() {
        if (!listenDirection) return this;
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);

        azimuth = Math.toDegrees(values[0]);
        pitch = Math.toDegrees(values[1]);
        roll = Math.toDegrees(values[2]);

        if ((pitch > -45 && pitch < 45) && ((roll > -45 && roll < 45) || roll < -135 || roll > 135)) {
            if (!cameraOrientation.equals("未知方向")) {
                cameraOrientation = "未知方向";
            }
            return this;
        }

        if ((pitch > -45 && pitch < 45) && (roll >= 45 && roll <= 135)) {
            String temp = calculateCameraOrientation(azimuth - 90 > -180 ? (azimuth - 90) : (azimuth + 270));
            if (!cameraOrientation.equals(temp)) {
                cameraOrientation = temp;
            }
            return this;
        }
        if ((pitch > -45 && pitch < 45) && (roll >= -135 && roll <= -45)) {
            String temp = calculateCameraOrientation(azimuth + 90 < 180 ? (azimuth + 90) : (270 - azimuth));
            if (!cameraOrientation.equals(temp)) {
                cameraOrientation = temp;
            }
            return this;
        }
        if ((pitch > -90 && pitch < -45)) {
            //屏幕向上 机头方向就是摄像头方向
            if (roll > -90 && roll < 90) {
                String temp = calculateCameraOrientation(azimuth);
                if (!cameraOrientation.equals(temp)) {
                    cameraOrientation = temp;
                }
            }
            //屏幕向下
            if (roll < -90 || roll > 90) {
                String temp = null;
                if (azimuth > -180 && azimuth < -90) {
                    temp = calculateCameraOrientation(azimuth + 180);
                }
                if (azimuth > 90 && azimuth < 180) {
                    temp = calculateCameraOrientation(azimuth - 180);
                }
                if (azimuth > 0 && azimuth < 90) {
                    temp = calculateCameraOrientation(azimuth - 180);
                }
                if (azimuth > -90 && azimuth < 0) {
                    temp = calculateCameraOrientation(azimuth + 180);
                }
                if (!cameraOrientation.equals(temp)) {
                    cameraOrientation = temp;
                }
            }
            return this;
        }
        //机头向下
        if ((pitch > 45 && pitch < 90)) {
            //屏幕向上
            if (roll > -90 && roll < 90) {
                String temp = null;
                if (azimuth > -180 && azimuth < -90) {
                    temp = calculateCameraOrientation(azimuth + 180);
                }
                if (azimuth > 90 && azimuth < 180) {
                    temp = calculateCameraOrientation(azimuth - 180);
                }
                if (azimuth > 0 && azimuth < 90) {
                    temp = calculateCameraOrientation(azimuth - 180);
                }
                if (azimuth > -90 && azimuth < 0) {
                    temp = calculateCameraOrientation(azimuth + 180);
                }
                if (!cameraOrientation.equals(temp)) {
                    cameraOrientation = temp;
                }
            }
            //屏幕向下
            if (roll < -90 || roll > 90) {
                String temp = calculateCameraOrientation(azimuth);
                if (!cameraOrientation.equals(temp)) {
                    cameraOrientation = temp;
                }
            }
            return this;
        }
        return this;
    }


    // 计算方向(手机机头指向位置)
    private String calculateCameraOrientation(double azimuth) {
        if (azimuth >= -5 && azimuth < 5) {
            System.out.println("北");
            headOrientation = "北";
        } else if (azimuth >= 5 && azimuth < 85) {
            System.out.println("东北");
            headOrientation = "东北";
        } else if (azimuth >= 85 && azimuth <= 95) {
            System.out.println("正东");
            headOrientation = "正东";
        } else if (azimuth >= 95 && azimuth < 175) {
            System.out.println("东南");
            headOrientation = "东南";
        } else if ((azimuth >= 175 && azimuth <= 180)
                || azimuth >= -180 && azimuth < -175) {
            System.out.println("正南");
            headOrientation = "正南";
        } else if (azimuth >= -175 && azimuth < -95) {
            System.out.println("西南");
            headOrientation = "西南";
        } else if (azimuth >= -95 && azimuth < -85) {
            System.out.println("正西");
            headOrientation = "正西";
        } else if (azimuth >= -85 && azimuth < -5) {
            System.out.println("西北");
            headOrientation = "西北";
        } else {
            headOrientation = "未知方向";
        }
        return headOrientation;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
        }
        computePhoneGesture();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //获取机头方向
    public String getDeviceHeadFront() {
        return headOrientation;
    }

    //获取机背方向
    public String getDeviceBackFront() {
        return cameraOrientation;
    }

    //获取机面方向
    public String getDeviceFaceFront() {
        if (cameraOrientation.equals("未知方向")) {
            faceOrientation = "未知方向";
        }
        if (cameraOrientation.equals("北")) {
            faceOrientation = "南";
        }
        if (cameraOrientation.equals("东北")) {
            faceOrientation = "西南";
        }
        if (cameraOrientation.equals("东")) {
            faceOrientation = "西";
        }
        if (cameraOrientation.equals("东南")) {
            faceOrientation = "西北";
        }
        if (cameraOrientation.equals("南")) {
            faceOrientation = "北";
        }
        if (cameraOrientation.equals("西南")) {
            faceOrientation = "东北";
        }
        if (cameraOrientation.equals("西")) {
            faceOrientation = "东";
        }
        if (cameraOrientation.equals("西北")) {
            faceOrientation = "东南";
        }
        return faceOrientation;
    }


    public interface IOrientChange {
        public void orientChange();
    }

    public void setOrientChange(IOrientChange iOrientChange) {
        this.iOrientChange = iOrientChange;
    }
}
