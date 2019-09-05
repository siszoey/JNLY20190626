package com.lib.bandaid.widget.collect.image;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lib.bandaid.R;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;
import com.lib.bandaid.util.ObjectUtil;

public class CollectImgAdapter extends BaseRecycleAdapter<CollectImgBean, BaseViewHolder<CollectImgBean>> {

    public CollectImgAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    public BaseViewHolder<CollectImgBean> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent, R.layout.widget_collect_img_list_item);
    }

    class Holder extends BaseViewHolder<CollectImgBean>
            implements View.OnClickListener,
            CompoundButton.OnCheckedChangeListener {

        private CheckBox cbSel;
        private TextView tvDesc;
        private ImageView ivImage, ivMore, ivRemove;

        public Holder(ViewGroup parent, int resId) {
            super(parent, resId);

            cbSel = $(R.id.cbSel);
            ivMore = $(R.id.ivMore);
            tvDesc = $(R.id.tvDesc);
            ivImage = $(R.id.ivImage);
            ivRemove = $(R.id.ivRemove);

            ivMore.setOnClickListener(this);
            ivImage.setOnClickListener(this);
            ivRemove.setOnClickListener(this);
            cbSel.setOnCheckedChangeListener(this);
        }

        @Override
        public void setData(CollectImgBean data, int position) {
            tvDesc.setText(ObjectUtil.removeNull(data.getDesc()));
            Glide.with(context)
                    .load(data.getUri())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(context.getResources().getDrawable(R.mipmap.widget_picker_image_pick_img_null))
                    .into(ivImage);
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) iViewClickListener.onClick(v, data, position);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    }
}
