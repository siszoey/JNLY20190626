package com.lib.bandaid.widget.pick;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lib.bandaid.R;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/8/10.
 */

public class ImgPickAdapter extends BaseRecycleAdapter<String, BaseViewHolder<String>> {

    private final int ITEM_CAMERA = 1000;
    private final int ITEM_IMAGE = 2000;
    private String parentPath;

    private List<Integer> _chooseList;

    public ImgPickAdapter(@Nullable RecyclerView recyclerView) {
        super(recyclerView);
        _chooseList = new ArrayList<>();
    }

    public void setImgParentPath(String path) {
        this.parentPath = path;
    }

    public String getParentPath() {
        return parentPath;
    }

    @Override

    public BaseViewHolder<String> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_CAMERA) {
            return new HolderCamera(parent, R.layout.widget_picker_image_pick_camera);
        }
        if (viewType == ITEM_IMAGE) {
            return new HolderImage(parent, R.layout.widget_picker_image_pick_image);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_CAMERA;
        }
        return ITEM_IMAGE;
    }

    private class HolderCamera extends BaseViewHolder<String> implements View.OnClickListener {

        private ImageView ivCamera;

        public HolderCamera(ViewGroup parent, int resId) {
            super(parent, resId);
            ivCamera = $(R.id.ivCamera);
            ivCamera.setOnClickListener(this);
        }

        @Override
        public void setData(String data, int position) {
            System.out.println(data);
        }

        @Override
        public void onClick(View v) {
            if (iViewClickListener != null) {
                iViewClickListener.onClick(v, data, position);
            }
        }
    }

    private boolean isBatch = true;

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    private CheckBox cbImageLastCheck;

    private class HolderImage extends BaseViewHolder<String> implements View.OnClickListener, View.OnLongClickListener {

        private String imgPath;

        private ImageView ivImage;
        private CheckBox cbImage;

        public HolderImage(ViewGroup parent, int resId) {
            super(parent, resId);
            ivImage = $(R.id.ivImage);
            cbImage = $(R.id.cbImage);
            ivImage.setOnClickListener(this);
            ivImage.setOnLongClickListener(this);
        }

        @Override
        public void setData(String data, int position) {
            imgPath = parentPath + File.separator + data;
            Glide.with(context).load(imgPath).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(context.getResources().getDrawable(R.mipmap.widget_picker_image_pick_img_null)).into(ivImage);
            if (_chooseList.contains(position)) {
                cbImage.setChecked(true);
            } else {
                cbImage.setChecked(false);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ivImage) {
                cbImage.setChecked(!cbImage.isChecked());
                if (!isBatch) {
                    _chooseList.clear();
                    if (cbImageLastCheck != null && !cbImage.equals(cbImageLastCheck))
                        cbImageLastCheck.setChecked(!cbImageLastCheck.isChecked());
                    cbImageLastCheck = cbImage;
                }
                if (cbImage.isChecked()) {
                    if (!_chooseList.contains(position)) {
                        if (_chooseList.size() > 8) {
                            cbImage.setChecked(false);
                            Toast.makeText(context, "选择图片数量不能不能大于9张！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        _chooseList.add(position);
                    }
                } else {
                    if (_chooseList.contains(position)) {
                        _chooseList.remove((Integer) position);
                    }
                }
            }
            if (iViewClickListener != null) {
                iViewClickListener.onClick(v, imgPath, position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (iLongViewClickListener != null) {
                iLongViewClickListener.onLongViewClick(v, imgPath, position);
            }
            return true;
        }
    }

    public List<String> getPickedItem() {
        return getItems(_chooseList);
    }

    public ArrayList<String> getPicks() {
        List<String> picks = getItems(_chooseList);
        if (picks == null || picks.size() == 0) return null;
        ArrayList res = new ArrayList<>();
        for (String name : picks) {
            res.add(parentPath.concat(File.separator).concat(name));
        }
        return res;
    }
}
