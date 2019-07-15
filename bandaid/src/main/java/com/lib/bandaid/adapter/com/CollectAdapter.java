package com.lib.bandaid.adapter.com;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.R;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.bean.file.CollectFile;
import com.lib.bandaid.widget.other.UploadImageView;


/**
 * Created by zy on 2019/6/9.
 */

public class CollectAdapter extends BaseRecycleAdapter<CollectFile, BaseViewHolder<CollectFile>> {

    public CollectAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<CollectFile> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.item_select_picture);
    }

    class Holder extends BaseViewHolder<CollectFile> implements View.OnClickListener {
        UploadImageView iv_picture;
        ImageView iv_picture_close;


        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);
            iv_picture = getView(R.id.iv_picture);
            iv_picture_close = getView(R.id.iv_picture_close);
            iv_picture_close.setOnClickListener(this);
        }

        @Override
        public void setData(CollectFile data, int position) {
            if (data == null) return;
            iv_picture.setImgPath(data.getPath(), data.getName(), data.getDesc(), R.mipmap.ic_pic_dir);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_picture_close) {
                removeItem(position);
            }
        }
    }
}
