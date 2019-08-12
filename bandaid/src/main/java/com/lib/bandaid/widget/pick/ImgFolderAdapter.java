package com.lib.bandaid.widget.pick;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lib.bandaid.R;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;

/**
 * Created by zy on 2018/8/12.
 */

public class ImgFolderAdapter extends BaseRecycleAdapter<ImgFolderBean, BaseViewHolder<ImgFolderBean>> {

    public ImgFolderAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }


    @Override
    public BaseViewHolder<ImgFolderBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.widget_picker_image_pick_dir_list_item);
    }


    private class Holder extends BaseViewHolder<ImgFolderBean> implements View.OnClickListener {

        private RelativeLayout llParent;
        private ImageView ivImage;
        private TextView tvImageDirName, tvImageDirCount;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            llParent = $(R.id.llParent);
            ivImage = $(R.id.ivImage);
            tvImageDirName = $(R.id.tvImageDirName);
            tvImageDirCount = $(R.id.tvImageDirCount);
            llParent.setOnClickListener(this);
        }

        @Override
        public void setData(ImgFolderBean data, int position) {
            tvImageDirName.setText(data.getName());
            tvImageDirCount.setText(data.getCount() + "张");
            Glide.with(context).load(data.getFirstImagePath())
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(context.getResources().getDrawable(R.mipmap.ic_pic_dir)).into(ivImage);
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) {
                iViewClickListener.onClick(v, data, position);
            }
        }
    }
}
