package com.lib.bandaid.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.util.MaterialDialogUtil;
import com.lib.bandaid.util.MeasureScreen;
import com.lib.bandaid.util.ViewUtil;
import com.lib.bandaid.R;
import com.lib.bandaid.widget.layout.RootStatusView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by zy on 2019/4/24.
 * 模板activity 内置title
 */

public abstract class BaseTemplateAty extends AppCompatActivity {

    protected Context _context;
    protected Activity _activity;
    protected String _titleName;
    protected Toolbar _toolbar;
    protected TextView _tvToolbarName;
    protected RootStatusView _frameLayout;
    protected View _contentView;
    protected Button _btnRight;

    MaterialDialog dialog;
    /**
     * 对话框计数器
     */
    Integer dialogCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_template_app_compat_activity);
        EventBus.getDefault().register(this);
        init();
    }

    //防止子类不写导致运行出错
    @Subscribe
    public void onEventMainThread(Object object) {

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void init() {
        _context = this;
        _activity = this;
        _toolbar = findViewById(R.id._toolbar);
        _tvToolbarName = findViewById(R.id._tvToolbarName);
        _frameLayout = findViewById(R.id._frameLayout);
        _btnRight = findViewById(R.id.btnRight);
        initTitle();
    }

    void initTitle() {
        _toolbar.setTitle(R.string.app_name);
        setSupportActionBar(_toolbar);
    }

    protected void initTitle(Integer leftIcon, String name, int gravity) {
        this._titleName = name;
        _toolbar.setTitle("");
        _tvToolbarName.setGravity(gravity);
        _tvToolbarName.setText(name);
        if (leftIcon != null) {
            _toolbar.setNavigationIcon(leftIcon);
        }
    }

    protected void setToolbarRight(String text, @Nullable Integer icon, View.OnClickListener btnClick) {
        _btnRight.setVisibility(View.VISIBLE);
        if (text != null) {
            _btnRight.setText(text);
        }
        if (icon != null) {
            _btnRight.setBackgroundResource(icon.intValue());
            ViewGroup.LayoutParams linearParams = _btnRight.getLayoutParams();
            linearParams.height = MeasureScreen.dip2px(this, 26);
            linearParams.width = MeasureScreen.dip2px(this, 26);
            _btnRight.setLayoutParams(linearParams);
        }
        _btnRight.setOnClickListener(btnClick);
    }

    public void setContentView(int layoutResID) {
        _frameLayout.removeAllViews();
        _contentView = View.inflate(this, layoutResID, null);
        _frameLayout.addView(_contentView);
        onContentChanged();
        viewBindCompleted();

        initialize();
        registerEvent();
        initClass();
    }

    public void setContentViewReplace(int layoutResID) {
        super.setContentView(layoutResID);
        onContentChanged();
        viewBindCompleted();
        initialize();
        registerEvent();
        initClass();
    }

    protected void viewBindCompleted() {

    }

    protected abstract void initialize();
    protected abstract void registerEvent();
    protected abstract void initClass();


    //----------------------------------------------------------------------------------------------
    protected void setOnRetryClickListener(View.OnClickListener listener) {
        _frameLayout.setOnRetryClickListener(listener);
    }

    protected void setOnViewStatusChangeListener(RootStatusView.OnViewStatusChangeListener listener) {
        _frameLayout.setOnViewStatusChangeListener(listener);
    }

    protected void showContent() {
        _frameLayout.showContent();
    }

    protected void showNoNetwork() {
        _frameLayout.showNoNetwork();
    }

    protected void showEmpty() {
        _frameLayout.showEmpty();
    }

    protected void showLoading() {
        _frameLayout.showLoading();
    }

    protected void showError() {
        _frameLayout.showError();
    }
    //----------------------------------------------------------------------------------------------


    /**
     * *********************************************************************************************
     * findViewById
     * *********************************************************************************************
     */
    public <T extends View> T $(int resId) {
        return ViewUtil.findViewById(this, resId);
    }

    /**
     * 调整activity大小比例
     *
     * @param w
     * @param h
     */
    protected void adjustActivitySize(float w, float h) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (MeasureScreen.getScreenWidth(this) * w);
        lp.height = (int) (MeasureScreen.getScreenHeight(this) * h);
        getWindow().setAttributes(lp);
    }

    protected void showProgressDialog() {
        if (dialog == null) dialog = MaterialDialogUtil.showLoadProgress(this, "加载中...", true);
        synchronized (dialogCount) {
            dialogCount++;
        }
        if (dialogCount == 1 && !dialog.isShowing() && !isFinishing()) {
            dialog.show();
        }
    }

    protected void stopProgressDialog() {
        synchronized (dialogCount) {
            if (dialogCount > 0) dialogCount--;
        }
        if (dialogCount == 0 && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
