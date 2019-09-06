package com.lib.bandaid.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.lib.bandaid.R;

public final class ImgUtil {


    public static void simpleLoadImg(ImageView view, String uri) {
        if (view == null) return;
        // 在需要重新获取更新的图片时调用
        Context context = view.getContext();
        String updateTime = String.valueOf(System.currentTimeMillis());
        Glide.with(context).load(uri).signature(new StringSignature(updateTime)).skipMemoryCache(true).diskCacheStrategy
                (DiskCacheStrategy.NONE).error(context.getResources().getDrawable(R.mipmap.ic_pic_dir)).into(view);
    }
}
