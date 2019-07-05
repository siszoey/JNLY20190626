/*
 * Copyright (c) 2015.
 * 湖南球谱科技有限公司版权所有
 * Hunan Qiupu Technology Co., Ltd. all rights reserved.
 */

package com.camera.lib.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.camera.lib.R;
import com.camera.lib.view.photoview.PhotoView;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private View view;
    private List<ImagePagerBean> mImages;
    private Context mContext;
    private OnViewPagerAdapterListener mListener;

    public void setListener(OnViewPagerAdapterListener listener) {
        mListener = listener;
    }

    public ViewPagerAdapter(List<ImagePagerBean> urls, Context context) {
        mImages = urls;
        mContext = context;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        view = (View) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public View getPrimaryItem() {
        return view;
    }

    public List<ImagePagerBean> getmImages() {
        return mImages;
    }

    public void setmImages(List<ImagePagerBean> mImages) {
        this.mImages = mImages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImagePagerBean bean = mImages.get(position);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.text_image_view_pager_item, container, false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.pv_image);
        photoView.enable();

        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(view.getContext())
                .load(bean.getSmallUrl())
                .crossFade();//加载缩略图;
        System.out.println(bean.toString());
        // 这里只是说明一下如何实现，具体的逻辑操作和封装需要根据需求自己编写代码，
        // 比如可以将最新一次的更新保存在SharedPreferences中，每次加载时跟Preferences中保存的数值进行对比
        String updateTime = String.valueOf(System.currentTimeMillis()); // 在需要重新获取更新的图片时调用
        Glide.with(view.getContext())
                .load(bean.getUrl())
                .signature(new StringSignature(updateTime))
                .thumbnail(thumbnailRequest)//加载缩略图
                .fitCenter()
                .crossFade()
                .error(R.drawable.ic_pic_dir).dontAnimate()
                .into(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mListener != null) {
                    mListener.onClickListener(arg0, position);
                }
            }
        });
        photoView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view1) {
                if (mListener != null) {
                    mListener.onLongClickListener(view1, position);
                }
                return false;
            }
        });
        container.addView(view);
        view.setId(position);
        return view;
    }

    public interface OnViewPagerAdapterListener {
        void onClickListener(View view, int position);

        void onLongClickListener(View view, int position);
    }


}