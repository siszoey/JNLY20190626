package com.camera.lib.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.camera.lib.R;
import com.camera.lib.util.MeasureScreen;
import com.camera.lib.util.NotifyArrayList;
import com.camera.lib.util.ScreenUtil;
import com.camera.lib.util.ViewUtil;
import com.camera.lib.widget.ImagePagerBean;
import com.camera.lib.widget.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/6/12.
 */

public class ImgScanDialog extends DialogFragment {

    protected float w = 1f;
    protected float h = 1f;
    protected Context context;

    private NotifyArrayList<File> files;
    private ViewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TextView tvDesc;
    private int curPosition;
    private ArrayList<ImagePagerBean> mImages;

    public static ImgScanDialog newInstance(NotifyArrayList<File> files) {
        ImgScanDialog fragment = new ImgScanDialog();
        fragment.setFiles(files);
        return fragment;
    }

    private void setFiles(NotifyArrayList<File> files) {
        this.files = files;
        mImages = new ArrayList<>();
        ImagePagerBean bean;
        for (int i = 0; i < files.size(); i++) {
            bean = new ImagePagerBean(files.get(i));
            mImages.add(bean);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_dialog_img_scan, null);
        viewPager = ViewUtil.findViewById(view, R.id.viewPager);
        tvDesc = ViewUtil.findViewById(view, R.id.tvDesc);
        initViewPager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adjustActivitySize(w, h);
        ScreenUtil.hideBottomUIMenu(((Activity) context).getWindow());
    }


    private void initViewPager() {
        pagerAdapter = new ViewPagerAdapter(mImages, context);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tvDesc.scrollTo(0, 0);
                String text = mImages.get(position + Math.round(positionOffset)).getDesc();

                if (TextUtils.isEmpty(text)) {
                    tvDesc.setVisibility(View.GONE);
                } else {
                    tvDesc.setVisibility(View.VISIBLE);
                    tvDesc.setText(text);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setNumTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pagerAdapter.setListener(new ViewPagerAdapter.OnViewPagerAdapterListener() {
            @Override
            public void onClickListener(View view, int position) {
            }

            @Override
            public void onLongClickListener(View view, int position) {
            }
        });

        viewPager.setCurrentItem(curPosition);
    }


    private void setNumTitle(int position) {
        curPosition = position;
    }

    protected void adjustActivitySize(float w, float h) {
        Dialog dialog = getDialog();
        if (dialog == null) return;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = (int) (MeasureScreen.getScreenWidth(context) * w);
        lp.height = (int) (MeasureScreen.getScreenHeight(context) * h);
        if (window != null) window.setLayout(lp.width, lp.height);
    }

    public ImgScanDialog show(@NonNull Context context) {
        FragmentManager manager = null;
        if (context instanceof FragmentActivity) {
            manager = ((FragmentActivity) context).getSupportFragmentManager();
        }
        if (manager == null) return this;
        super.show(manager, "");
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ScreenUtil.hideBottomUIMenu(((Activity) context).getWindow());
    }
}
