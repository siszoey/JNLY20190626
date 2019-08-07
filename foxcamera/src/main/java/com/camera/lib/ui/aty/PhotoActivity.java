package com.camera.lib.ui.aty;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import com.camera.lib.R;
import com.camera.lib.core.JCameraView;
import com.camera.lib.core.WaterMarkView;
import com.camera.lib.listener.ClickListener;
import com.camera.lib.listener.EasyAnimListener;
import com.camera.lib.listener.JCameraListener;
import com.camera.lib.util.BitmapUtil;
import com.camera.lib.util.DateUtil;
import com.camera.lib.util.DeviceGesture;
import com.camera.lib.util.FileUtil;
import com.camera.lib.util.NotifyArrayList;
import com.camera.lib.widget.ImagePagerBean;
import com.camera.lib.widget.ImageViewPager;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PhotoActivity extends BaseCameraAty implements JCameraListener, NotifyArrayList.IListener, WaterMarkView.IWaterMarkListener {

    private final static String CAMERA_PARENT_PATH = "CAMERA_PARENT_PATH";
    private final static String CAMERA_IS_NORMAL = "CAMERA_IS_NORMAL";
    private final static String CAMERA_SHOW_LOCAL = "CAMERA_SHOW_LOCAL";
    private final static String CAMERA_SHOW_ORIENT = "CAMERA_SHOW_ORIENT";
    private final static String CAMERA_WATER_MARK = "CAMERA_WATER_MARK";

    public static void start(Context context, boolean isNormal, String parentPath, boolean showLocal, boolean showOrient, LinkedHashMap waterMark) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(CAMERA_IS_NORMAL, isNormal);
        intent.putExtra(CAMERA_PARENT_PATH, parentPath);
        intent.putExtra(CAMERA_SHOW_LOCAL, showLocal);
        intent.putExtra(CAMERA_SHOW_ORIENT, showOrient);
        intent.putExtra(CAMERA_WATER_MARK, waterMark);
        PhotoActivity.waterMark = waterMark;
        context.startActivity(intent);
    }

    private NotifyArrayList<File> files = new NotifyArrayList<>(this);

    private String parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "0";
    private boolean isNormal;
    private boolean showLocal;
    private boolean showOrient;
    private static LinkedHashMap waterMark;
    private DeviceGesture deviceGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        {
            if (getIntent() != null) {
                isNormal = getIntent().getBooleanExtra(CAMERA_IS_NORMAL, false);
                parentPath = getIntent().getStringExtra(CAMERA_PARENT_PATH);
                if (parentPath == null)
                    parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "0";
                showLocal = getIntent().getBooleanExtra(CAMERA_SHOW_LOCAL, false);
                showOrient = getIntent().getBooleanExtra(CAMERA_SHOW_ORIENT, true);
            }
        }
        setContentView(R.layout.demo_ui_photo_aty);
    }

    @Override
    protected void initialize() {
        jCameraView = findViewById(R.id.jcameraview);
        if (showOrient) deviceGesture = DeviceGesture.getInstance(context);
    }

    @Override
    protected void registerEvent() {
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                PhotoActivity.this.finish();
            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                if (files.size() == 0) {
                    Toast.makeText(context, "还没有拍照!", Toast.LENGTH_LONG).show();
                    return;
                }
                ImageViewPager.start(context, ImagePagerBean.convert2Beans(files), 0);
            }
        });
    }

    @Override
    protected void initClass() {
        if (waterMark == null) {
            waterMark = new LinkedHashMap();
            waterMark.put("村名", "王家村");
            waterMark.put("纬度", "39.666666");
            waterMark.put("经度", "108.666666");
            waterMark.put("时间", DateUtil.dateTimeToStr(new Date()));
        }

        jCameraView.setJCameraLisenter(this).isNormal(isNormal);
        //设置既可以拍照又能录视频
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        jCameraView.setSaveVideoPath(parentPath);
        jCameraView.getWaterMarkView().setMarkListener(this);
        getPermissions();
    }

    @Override
    public void captureSuccess(Bitmap bitmap) {
        takePhotoSuccess(bitmap);
    }

    @Override
    public void recordSuccess(String url, Bitmap firstFrame) {

    }

    void takePhotoSuccess(Bitmap bitmap) {
        File file = FileUtil.saveBitmap(bitmap, parentPath);
        files.add(file);
    }

    @Override
    public void itemChange() {
        final ImageView rightImage = jCameraView.getRightView();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(rightImage, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(rightImage, "scaleY", 1f, 1.5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.setDuration(800);
        animatorSet.start();
        final File file = files.getLastItem();
        animatorSet.addListener(new EasyAnimListener() {

            @Override
            public void onAnimationEnd(Animator animation) {
                rightImage.setScaleType(ImageView.ScaleType.FIT_XY);
                if (file == null) {
                    jCameraView.reSetRightView();
                } else {
                    Bitmap bitmap = BitmapUtil.file2Bmp(file);
                    rightImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    rightImage.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Override
    public LinkedHashMap markListener() {
        waterMark.put("时间", DateUtil.dateTimeToStr(new Date()));
        if (deviceGesture != null) waterMark.put("方向", deviceGesture.getDeviceBackFront());
        //修改经纬度数据
        if (showLocal) {

        }
        return waterMark;
    }
}
