package com.lib.bandaid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.lib.bandaid.R;
import com.lib.bandaid.system.theme.aty.ATEActivity;
import com.lib.bandaid.system.theme.utils.ATE;
import com.lib.bandaid.utils.DialogFactory;
import com.lib.bandaid.utils.MeasureScreen;
import com.lib.bandaid.utils.ViewUtil;
import com.lib.bandaid.widget.layout.RootStatusView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/9/10.
 * 精简版
 */

public abstract class BaseAppCompatAty extends ATEActivity {
    protected Context _context;
    protected Activity _activity;
    protected String _titleName;
    protected AppBarLayout _appBarLayout;
    protected Toolbar _toolbar;
    protected TextView _tvToolbarName;
    protected RootStatusView _frameLayout;
    protected View _contentView;
    protected Button _btnRight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!ATE.config(this).isConfigured()) {
            ATE.config(this)
                    .primaryColor(getResources().getColor(R.color.colorPrimary))
                    .accentColor(getResources().getColor(R.color.colorAccent))
                    .coloredNavigationBar(true)
                    .commit();
        }
        _context = this;
        _activity = this;
        DialogFactory.getFactory().register(this);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.widget_base_app_compat_activity);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            if (iOnActivityResults != null) {
                iOnActivityResults.clear();
            }
            iOnActivityResults = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogFactory.getFactory().unRegister(this);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        ATE.applyMenu(_toolbar);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
        _appBarLayout = findViewById(R.id._appBarLayout);
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

        initialize();
        registerEvent();
        initClass();
    }

    public void setContentViewReplace(int layoutResID) {
        super.setContentView(layoutResID);
        onContentChanged();

        initialize();
        registerEvent();
        initClass();
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
     * activity结果回调
     * *********************************************************************************************
     */
    private List<IOnActivityResult> iOnActivityResults;

    public void addIOnActivityResults(IOnActivityResult iOnActivityResult) {
        if (iOnActivityResults == null) iOnActivityResults = new ArrayList<>();
        if (iOnActivityResults.contains(iOnActivityResult)) return;
        iOnActivityResults.add(iOnActivityResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (iOnActivityResults != null) {
            for (IOnActivityResult iOnActivityResult : iOnActivityResults) {
                iOnActivityResult.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * *********************************************************************************************
     * findViewById
     * *********************************************************************************************
     */
    public <T extends View> T $(int resId) {
        return ViewUtil.findViewById(this, resId);
    }

    protected void adjustActivitySize(float w, float h) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (MeasureScreen.getScreenWidth(this) * w);
        lp.height = (int) (MeasureScreen.getScreenHeight(this) * h);
        getWindow().setAttributes(lp);
    }
}
