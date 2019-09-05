package com.lib.bandaid.widget.pick;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camera.lib.ui.aty.PhotoActivity;
import com.camera.lib.widget.ImagePagerBean;
import com.camera.lib.widget.ImageViewPager;
import com.lib.bandaid.R;
import com.lib.bandaid.activity.BaseAppCompatActivity;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.thread.rx.RxSimpleObserver;
import com.lib.bandaid.thread.rx.RxSimpleSubscribe;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.ViewUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImgPickActivity extends BaseAppCompatActivity implements View.OnClickListener, BaseRecycleAdapter.IViewClickListener<String> {

    private TextView tvChooseDir;
    private TextView tvPreview;
    private RecyclerView rvImgPicker;

    //扫描拿到所有的图片文件夹
    private List<ImgFolderBean> listImgFolders;
    //当前文件夹中的图片数量
    private int picsSize;
    //图片数量最多的文件夹
    private ImgFolderBean imgFolderBean;
    private File imgDir;
    //图片文件的名称
    private List<String> imgNames;

    private ImgPickAdapter imgPickAdapter;

    private List<String> takePhoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCamera();
        setContentView(R.layout.widget_picker_image_aty_layout);
        initTitle(R.drawable.ic_back, "相册", Gravity.CENTER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void initialize() {
        rvImgPicker = $(R.id.rvImageList);
        tvChooseDir = $(R.id.tvChooseDir);
        tvPreview = $(R.id.tvPreview);
    }

    @Override
    public void registerEvent() {
        tvChooseDir.setOnClickListener(this);
        tvPreview.setOnClickListener(this);
    }

    @Override
    public void initClass() {
        rvImgPicker.setLayoutManager(new GridLayoutManager(this, 3));
        imgPickAdapter = new ImgPickAdapter(rvImgPicker);
        imgPickAdapter.setIViewClickListener(this);
        scanSystemImg();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvChooseDir) {
            initPopWin();
        }
        if (v.getId() == R.id.tvPreview) {
            getImgFromFolder(imgFolderBean);
            openImageViewPager();
        }
    }


    void scanSystemImg() {
        Observable.create(new RxSimpleSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                if (listImgFolders == null) scanImgDir();
                emitter.onNext(1);
                if (imgFolderBean != null) getImgFromFolder(imgFolderBean);
                emitter.onNext(2);
            }
        })
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSimpleObserver<Integer>() {
                    @Override
                    public void onNext(Integer i) {
                        if (i == 1) {
                            System.out.println("图片文件夹遍历完成");
                        }
                        if (i == 2) {
                            imgPickAdapter.setImgParentPath(imgFolderBean.getDir());
                            List<String> temp = new ArrayList<>(imgNames);
                            temp.add(0, "CAMERA");
                            imgPickAdapter.replaceAll(temp);
                        }
                    }
                });
    }


    private void scanImgDir() {
        Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        listImgFolders = new ArrayList<>();
        HashSet<String> dirPaths = new HashSet<>();
        // 只查询jpeg和png的图片
        Cursor cursor = getContentResolver().query(imgUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        String firstImage = null;   //获取的uri但是被转换为物理路径了
        while (cursor.moveToNext()) { // 获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            if (firstImage == null) {// 拿到第一张图片的路径
                firstImage = path;
            }
            File parentFile = new File(path).getParentFile(); // 获取该图片的父路径名
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            ImgFolderBean imageFolder = null;
            if (dirPaths.contains(dirPath)) {  // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                continue;
            } else {
                dirPaths.add(dirPath);
                imageFolder = new ImgFolderBean();// 初始化imageFolder
                imageFolder.setDir(dirPath);
                imageFolder.setFirstImagePath(path);
            }
            int picSize = 0;
            try {
                picSize = parentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                            return true;
                        }
                        return false;
                    }
                }).length;
            } catch (Exception e) {
                e.getMessage();
            }

            imageFolder.setCount(picSize);
            listImgFolders.add(imageFolder);
            if (picSize > picsSize) {
                picsSize = picSize;
                imgDir = parentFile;
                imgFolderBean = imageFolder;
            }
        }
        cursor.close();
        // 扫描完成，辅助的HashSet也就可以释放内存了
        dirPaths.clear();
        dirPaths = null;

        //判断优先显示的路径下有没有图片，有的话就优先显示，没有就不管他
        String firstPath = getIntent().getStringExtra("firstPath");
        if (firstPath != null && !firstPath.equals("")) {
            List<Object> list = FileUtil.getFileNameInFolder(firstPath, ".jpg", false);
            if (list != null && list.size() > 0) {
                picsSize = list.size();
                imgDir = new File(firstPath);
                ImgFolderBean imageFolder = new ImgFolderBean();
                imageFolder.setDir(firstPath);
                imageFolder.setFirstImagePath(list.get(0).toString());
                imgFolderBean = imageFolder;
            }
        }
    }


    private void getImgFromFolder(ImgFolderBean imgFolderBean) {
        String parentPath = imgFolderBean.getDir();
        imgDir = new File(parentPath);//将路径转换为文件
        imgNames = Arrays.asList(imgDir.list(new FilenameFilter() {//获取当前文件夹里的文件名
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) {
                    return true;
                }
                return false;
            }
        }));
    }

    /**
     * 预览图片
     */
    private void openImageViewPager() {
        List<String> temp = imgPickAdapter.getPickedItem();
        if (temp == null) return;
        String path = imgPickAdapter.getParentPath();
        if (path == null) return;
        ArrayList<ImagePagerBean> imagePagerBeans = new ArrayList<>();
        for (String item : temp) {
            String path_img = path + "/" + item;
            ImagePagerBean imagePagerBean = new ImagePagerBean(path_img, "预览", path_img);
            imagePagerBeans.add(imagePagerBean);
        }
        if (imagePagerBeans.size() > 0) {
            ImageViewPager.start(this, imagePagerBeans, 0);
        }
    }


    private void initPopWin() {
        ImgFolderAdapter imgFolderAdapter;
        View view = LayoutInflater.from(_context).inflate(R.layout.widget_picker_image_pick_dir, null);
        TextView tvDismiss = ViewUtil.findViewById(view, R.id.tvDismiss);
        RecyclerView rvDir = ViewUtil.findViewById(view, R.id.rvImgDir);
        imgFolderAdapter = new ImgFolderAdapter(rvDir);
        imgFolderAdapter.replaceAll(listImgFolders);
        PopupWindow popWindow = new PopupWindow(view, ViewUtil.MATCH_PARENT, ViewUtil.WRAP_CONTENT);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable(0x30000000));
        popWindow.setAnimationStyle(R.style.PopupAnimBottom);
        popWindow.showAtLocation(tvChooseDir, Gravity.BOTTOM, 0, 0);
        tvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        imgFolderAdapter.setIViewClickListener(new BaseRecycleAdapter.IViewClickListener<ImgFolderBean>() {
            @Override
            public void onClick(View view, ImgFolderBean data, int position) {
                imgFolderBean = data;
                scanSystemImg();
                popWindow.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view, String data, int position) {
        if (position == 0) {
            PhotoActivity.start(_activity, 1000, isNormal, parentPath, showLocal, showOrient, waterMark);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 1000) {
            //表明拍了照片
            takePhoto = data.getStringArrayListExtra(PhotoActivity.IMG);
            finish();
        }
    }

    @Override
    public void finish() {
        ArrayList<String> sel = imgPickAdapter.getPicks();
        if (ObjectUtil.isEmpty(sel) && ObjectUtil.isEmpty(takePhoto)) super.finish();
        //表明已经选择了图片
        ArrayList<String> picks = new ArrayList<>();
        if (!ObjectUtil.isEmpty(sel)) picks.addAll(sel);
        if (!ObjectUtil.isEmpty(takePhoto)) picks.addAll(takePhoto);
        Intent back = new Intent();
        back.putExtra(IMG, picks);
        setResult(RESULT_OK, back);
        super.finish();
    }

    //----------------------------------------------------------------------------------------------
    public final static String IMG = "IMG";
    private final static String CAMERA_PARENT_PATH = "CAMERA_PARENT_PATH";
    private final static String CAMERA_IS_NORMAL = "CAMERA_IS_NORMAL";
    private final static String CAMERA_SHOW_LOCAL = "CAMERA_SHOW_LOCAL";
    private final static String CAMERA_SHOW_ORIENT = "CAMERA_SHOW_ORIENT";

    public static void start(Activity activity, @NonNull int reqCode, boolean isNormal, String parentPath, boolean showLocal, boolean showOrient, LinkedHashMap waterMark) {
        Intent intent = new Intent(activity, ImgPickActivity.class);
        intent.putExtra(CAMERA_IS_NORMAL, isNormal);
        intent.putExtra(CAMERA_PARENT_PATH, parentPath);
        intent.putExtra(CAMERA_SHOW_LOCAL, showLocal);
        intent.putExtra(CAMERA_SHOW_ORIENT, showOrient);
        ImgPickActivity.waterMark = waterMark;
        activity.startActivityForResult(intent, reqCode);
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
        FileUtil.usePathSafe(parentPath);
    }


}
