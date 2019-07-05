package com.camera.lib.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import androidx.annotation.NonNull;
import com.camera.lib.util.MeasureScreen;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zy on 2019/6/12.
 */

public class WaterMarkView extends View {

    private final float left = 100;
    private final float bottom = 210;

    private Context context;
    private int screenWidth;
    private int screenHeight;
    private Paint paint;
    private int rotate = 0;
    private boolean noMark = false;
    private LinkedHashMap waterMark;
    private float totalHigh;
    private Timer timer = new Timer(true);
    private Map<String, Float> markPos = new HashMap<>();


    public WaterMarkView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WaterMarkView setRotate(int rotate) {
        this.rotate = rotate;
        postInvalidate();
        return this;
    }

    public WaterMarkView noWaterMark(boolean noMark) {
        this.noMark = noMark;
        postInvalidate();
        return this;
    }

    void init() {
        screenWidth = MeasureScreen.getScreenWidth(context);
        screenHeight = MeasureScreen.getScreenHeight(context);
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        double fontSize = screenWidth * 0.035;
        markPos.put("fontSize", (float) fontSize);
        paint.setTextSize((float) fontSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        paint.setAntiAlias(true);  //抗锯齿
        paint.setStrokeWidth(0);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.FILL); //空心
        paint.setColor(Color.WHITE);//采用的颜色
        if (waterMark == null) {
            waterMark = new LinkedHashMap();
            waterMark.put("名称", "李家村");
            waterMark.put("地点", "李家村");
        }
        measureText(waterMark);
        timer.schedule(task, 0, 200);
    }

    synchronized void measureText(LinkedHashMap waterMark) {
        markPos.put("left", left);
        markPos.put("bottom", bottom);
        if (waterMark == null) return;
        float[] size = null;
        totalHigh = 0;
        for (Object key : waterMark.keySet()) {
            String text = key + ":" + waterMark.get(key);
            size = MeasureScreen.measureText(paint, text);
            totalHigh += size[1];
        }
        totalHigh = totalHigh + bottom;
        markPos.put("high", totalHigh);
        markPos.put("lineHigh", size[1]);
        markPos.put("lineSpan", paint.getTextSize() / 2);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (noMark) return;
        if (waterMark == null) return;
        float rowHigh = screenHeight - totalHigh;
        float edge = left;
        if (rotate == 0) {
            edge = left;
            rowHigh = screenHeight - totalHigh;
            canvas.rotate(0, screenWidth / 2, screenHeight / 2);
        }
        if (rotate == 90) {
            edge = left - (screenHeight - screenWidth) / 2;
            rowHigh = (screenHeight - screenWidth) / 2 + screenWidth - totalHigh;
            canvas.rotate(rotate, screenWidth / 2, screenHeight / 2);
        }
        if (rotate == -90) {
            edge = left - (screenHeight - screenWidth) / 2;
            rowHigh = (screenHeight - screenWidth) / 2 + screenWidth - totalHigh;
            canvas.rotate(rotate, screenWidth / 2, screenHeight / 2);
        }
        markPos.put("rotate", (float) rotate);

        String text;
        float[] size;
        int i = 0;
        for (Object key : waterMark.keySet()) {
            i++;
            text = key + "：" + waterMark.get(key);
            size = MeasureScreen.measureText(paint, text);
            rowHigh += size[1];
            canvas.drawText(text, edge, rowHigh + markPos.get("lineSpan") * i, paint);
        }
    }

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            if (iWaterMarkListener != null) {
                waterMark = iWaterMarkListener.markListener();
            }
            measureText(waterMark);
            postInvalidate();
        }
    };

    public LinkedHashMap getWaterMark() {
        return waterMark;
    }

    public Map<String, Float> getMarkPos() {
        return markPos;
    }

    IWaterMarkListener iWaterMarkListener;

    public void setMarkListener(IWaterMarkListener iWaterMarkListener) {
        this.iWaterMarkListener = iWaterMarkListener;
    }

    public interface IWaterMarkListener {
        public LinkedHashMap markListener();
    }

}
