package com.camera.lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zy on 2016/11/16.
 */

public class BitmapUtil {

    /**
     * 转换图片转换成圆角.
     *
     * @param bitmap 传入Bitmap对象
     * @return the bitmap
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 将图片圆形化
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = null;
        if (bitmap != null) {

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }
            output = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(output);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right,
                    (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top,
                    (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);

            bitmap = output;
            return bitmap;
        }
        output = null;
        return null;
    }

    /**
     * bitmap转byte数组
     *
     * @param bitmap
     * @return
     */
    public byte[] bitmap2Array(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
        bitmap.recycle();//自由选择是否进行回收
        byte[] result = output.toByteArray();//转换成功了
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * bitmap转File
     *
     * @param bitmap
     * @param file
     * @return
     */
    public static void bitmap2File(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * bitmap转File
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static boolean bitmap2File(Bitmap bitmap, String path) {

        int lastIndex = path.lastIndexOf("/");
        String parentPath = path.substring(0, lastIndex);
        File f = new File(parentPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        boolean flag = false;
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int fileS = bitmap.getByteCount();
            double z = (double) fileS / 1048576;
            if (z > 0.5) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }
            bos.flush();
            bos.close();
            flag = true;
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (IOException e) {
            new Throwable(e);
            flag = false;
        }
        return flag;
    }

    /**
     * bitmap转File
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static boolean bitmap2File(Bitmap bitmap, String path, int quality) {

        int lastIndex = path.lastIndexOf("/");
        String parentPath = path.substring(0, lastIndex);
        File f = new File(parentPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        boolean flag = false;
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int fileS = bitmap.getByteCount();
            double z = (double) fileS / 1048576;
            if (z > 0.5) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }
            //bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            flag = true;
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (IOException e) {
            new Throwable(e);
            flag = false;
        }
        return flag;
    }

    /* 旋转图片
    * @param angle
    * @param bitmap
    * @return Bitmap
    */
    public static Bitmap rotatingBitmap(Bitmap bitmap, int angle) {
        //modify by clj 20181115 角度为0，就不旋转
        if (angle == 0) return bitmap;
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return resizedBitmap;
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap bitmapCropWithRect(Bitmap bitmap, Rect rect) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        // 下面这句是关键
        Bitmap bmp = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height(), null, false);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }

    /**
     * 成功返回相对路径
     *
     * @param bitmapArray
     * @param path        绝对路径
     * @return
     */
    public static String byte2File(byte[] bitmapArray, String path) {
        try {
            if (bitmapArray != null) {
                /**
                 * 创建父路径
                 */
                int lastIndex = path.lastIndexOf("/");
                String pathPath = path.substring(0, lastIndex);
                File f = new File(pathPath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
                File file1 = new File(path);
                FileOutputStream outputStream = new FileOutputStream(file1);
                outputStream.write(bitmapArray, 0, bitmapArray.length);
                outputStream.close();
                return path;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap file2Bmp(File file) {
        Bitmap map = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getPath(), opts);
            int len = Math.max(opts.outHeight, opts.outWidth);
            int sampleSize = Math.round((float) len / 1000);
            if (sampleSize <= 0) sampleSize = 1;
            opts.inSampleSize = sampleSize;
            opts.inJustDecodeBounds = false;
            map = BitmapFactory.decodeFile(file.getPath(), opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Bitmap path2Bmp(String path) {
        Bitmap map = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int len = Math.max(opts.outHeight, opts.outWidth);
            int sampleSize = Math.round((float) len / 1000);
            if (sampleSize <= 0) sampleSize = 1;
            opts.inSampleSize = sampleSize;
            opts.inJustDecodeBounds = false;
            map = BitmapFactory.decodeFile(path, opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 根据路径 转bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getBitMBitmap(String path) {

        Bitmap map = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int len = Math.max(opts.outHeight, opts.outWidth);
            int sampleSize = Math.round((float) len / 1000);
            if (sampleSize <= 0) sampleSize = 1;
            opts.inSampleSize = sampleSize;
            opts.inJustDecodeBounds = false;
            map = BitmapFactory.decodeFile(path, opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取网络图片bitmap
     *
     * @param bitmap
     * @param url
     * @return
     */
    public Bitmap returnBitMap(Bitmap bitmap, final String url) {
        URL imageurl = null;
        try {
            imageurl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 给bitmap加水印
     *
     * @param bitmap
     * @param mark
     * @param pos
     * @return
     */
    public static Bitmap bmpMark(Bitmap bitmap, LinkedHashMap mark, Map pos) {
        //获取原始图片与水印图片的宽与高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        {
            //float fontSize = (float) pos.get("fontSize");
            int w = width < height ? width : height;
            double fontSize = w * 0.035;
            paint.setTextSize((float) fontSize);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
            paint.setAntiAlias(true);  //抗锯齿
            paint.setStrokeWidth(0);
            paint.setAlpha(100);
            paint.setStyle(Paint.Style.FILL); //空心
            paint.setColor(Color.WHITE);//采用的颜色
        }

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        //向位图中开始画入MBitmap原始图片
        canvas.drawBitmap(bitmap, 0, 0, null);
        //水印的位置坐标
        if (mark != null) {
            String text;
            int i = 0;
            float left = (float) pos.get("left");
            //float high = (float) pos.get("high");
            float bottom = (float) pos.get("bottom");
            float lineHigh = (float) pos.get("lineHigh");
            float lineSpan = (float) pos.get("lineSpan");
            float top = height - lineHigh * mark.size() - lineSpan * (mark.size() - 1);
            float[] size;
            for (Object key : mark.keySet()) {
                text = key + "：" + mark.get(key);
                size = MeasureScreen.measureText(paint, text);
                top += size[1];
                canvas.drawText(text, left / 2, top + lineSpan * i, paint);
                i++;
            }
        }
        canvas.save();
        canvas.restore();
        if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
        return newBitmap;

    }

}
