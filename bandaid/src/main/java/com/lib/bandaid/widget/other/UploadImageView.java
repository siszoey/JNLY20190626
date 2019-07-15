package com.lib.bandaid.widget.other;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.lib.bandaid.R;
import com.lib.bandaid.bean.file.CollectFile;

import java.io.File;

/**
 * Created by Administrator on 2018/7/16.
 * 上传下载的图片控件
 */

public class UploadImageView extends AppCompatImageView {

    private String imgPath;

    private String basePath;

    private Context context;

    private CollectFile collectFile;

    public UploadImageView(Context context) {
        super(context);
        this.context = context;
    }

    public UploadImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public UploadImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void init(String basePath){
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getImgPath() {
        return imgPath == null ? "":imgPath;
    }
    //设置本地图片或全链接网络图片
    public void setImgPath(String imgPath,String name,String desc) {
        this.imgPath =imgPath;
        collectFile = new CollectFile(name, imgPath, desc);
        String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用
        Glide.with(context).load(this.imgPath) .signature(new StringSignature(updateTime)).skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.NONE).error(getResources().getDrawable(R.mipmap.ic_pic_dir)).into(this);
    }
    //设置网络图片
    public void setImgPath(CollectFile c) {
        collectFile = c;
        imgPath =basePath+collectFile.getPath();
        String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用
        Glide.with(context).load(imgPath) .signature(new StringSignature(updateTime)).skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.NONE).error(getResources().getDrawable(R.mipmap.ic_pic_dir)).into(this);
    }
    //设置本地图片或全链接网络图片
    public void setImgPath(String imgPath,String name,String desc,int error) {
        this.imgPath =imgPath;
        collectFile = new CollectFile(name, imgPath, desc);
        String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用
        Glide.with(context).load(this.imgPath) .signature(new StringSignature(updateTime)).skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.NONE).error(getResources().getDrawable(error)).into(this);
    }
    //设置网络图片
    public void setImgPath(CollectFile c, int error) {
        collectFile = c;
        imgPath =basePath+collectFile.getPath();
        String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用
        Glide.with(context).load(imgPath) .signature(new StringSignature(updateTime)).skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.NONE).error(getResources().getDrawable(error)).into(this);
    }
    public CollectFile getCollectFile() {
        if (collectFile==null){
            collectFile = new CollectFile();
        }
        return collectFile;
    }


    public boolean haveImg(){
        if(imgPath==null){
            return false;
        }
        try{
            if(imgPath.contains("http")){
                return true;
            }else {
                File file =new File(imgPath);
                if(file.exists()){
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public  boolean delImg(String imgPath,String name,String desc){
        File file =new File(imgPath);
        file.delete();
        if(imgPath.contains("http")){
            setImgPath(imgPath,name,desc);
        }
        return true;
    }
    public  boolean delImg(String imgPath){
        File file =new File(imgPath);
        file.delete();
        if(imgPath.contains("http")){
            setImgPath(imgPath,"","");
        }
        return true;
    }
    public  boolean delImg(CollectFile c, int error){
        File file =new File(c.getPath());
        file.delete();
      //  if(imgPath.contains("http")){
            setImgPath(c.getPath(),c.getName(),c.getDesc(),error);
       // }
        return true;
    }


    /**
     * 判断是本地图片还是网络图片
     */
    public boolean isNetImg(){
        if(imgPath==null){
            return false;
        }
        try{
            if(imgPath.contains("http")){
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
