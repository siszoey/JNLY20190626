package com.lib.bandaid.widget.collect.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camera.lib.widget.ImagePagerBean;
import com.camera.lib.widget.ImageViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lib.bandaid.R;
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.widget.pick.ImgPickActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 图片采集页面
 */
public class CollectImgAty extends BaseAppCompatActivity
        implements View.OnClickListener, BaseRecycleAdapter.IViewClickListener<CollectImgBean> {

    public final static String IMG = "IMG";

    private RecyclerView rvImages;
    private FloatingActionButton fabAdd;
    private CollectImgAdapter collectImgAdapter;

    private ArrayList<CollectImgBean> list;


    private final static String CAMERA_PARENT_PATH = "CAMERA_PARENT_PATH";
    private final static String CAMERA_IS_NORMAL = "CAMERA_IS_NORMAL";
    private final static String CAMERA_SHOW_LOCAL = "CAMERA_SHOW_LOCAL";
    private final static String CAMERA_SHOW_ORIENT = "CAMERA_SHOW_ORIENT";

    private final static String COLLECT_BEANS = "COLLECT_BEANS";

    public static void start(Activity activity, @NonNull int requestCode, ArrayList<CollectImgBean> beans, boolean isNormal, String parentPath, boolean showLocal, boolean showOrient, LinkedHashMap waterMark) {
        Intent intent = new Intent(activity, CollectImgAty.class);
        intent.putExtra(CAMERA_IS_NORMAL, isNormal);
        intent.putExtra(CAMERA_PARENT_PATH, parentPath);
        intent.putExtra(CAMERA_SHOW_LOCAL, showLocal);
        intent.putExtra(CAMERA_SHOW_ORIENT, showOrient);

        intent.putExtra(COLLECT_BEANS, beans);
        CollectImgAty.waterMark = waterMark;
        activity.startActivityForResult(intent, requestCode);
    }

    private boolean isNormal;
    private boolean showLocal;
    private boolean showOrient;
    private String parentPath;
    private static LinkedHashMap waterMark;


    private void initCamera() {
        if (getIntent() == null) return;
        isNormal = getIntent().getBooleanExtra(CAMERA_IS_NORMAL, false);
        parentPath = getIntent().getStringExtra(CAMERA_PARENT_PATH);
        if (parentPath == null)
            parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "0";
        showLocal = getIntent().getBooleanExtra(CAMERA_SHOW_LOCAL, false);
        showOrient = getIntent().getBooleanExtra(CAMERA_SHOW_ORIENT, true);
        list = (ArrayList<CollectImgBean>) getIntent().getSerializableExtra(COLLECT_BEANS);
        FileUtil.usePathSafe(parentPath);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCamera();
        initTitle(R.drawable.ic_back, "图片采集", Gravity.CENTER);
        setContentView(R.layout.widget_collect_img_aty_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void initialize() {
        fabAdd = $(R.id.fabAdd);
        rvImages = $(R.id.rvImages);
    }

    @Override
    public void registerEvent() {
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void initClass() {
        rvImages.setLayoutManager(new GridLayoutManager(_context, 3));
        collectImgAdapter = new CollectImgAdapter(rvImages);
        collectImgAdapter.replaceAll(list);
        collectImgAdapter.setIViewClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fabAdd) {
            ImgPickActivity.start(_activity, 1000, isNormal, parentPath, showLocal, showOrient, waterMark);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1000) {
            List<String> picks = data.getStringArrayListExtra(ImgPickActivity.IMG);
            List<CollectImgBean> list = CollectImgBean.convertFromList(picks);
            collectImgAdapter.appendList(list);
        }
    }

    @Override
    public void onClick(View view, CollectImgBean data, int position) {
        //移除图片
        if (view.getId() == R.id.ivRemove) {
            collectImgAdapter.removeItem(position);
        }
        //图片预览
        if (view.getId() == R.id.ivImage) {
            ArrayList<ImagePagerBean> beans = CollectImgBean.convertToScanList(collectImgAdapter.getDataList());
            ImageViewPager.start(_context, beans, position);
        }
    }

    @Override
    public void finish() {
        ArrayList<CollectImgBean> data = collectImgAdapter.getDataArrayList();
        Intent back = new Intent();
        back.putExtra(IMG, data);
        setResult(RESULT_OK, back);
        super.finish();
    }
}
