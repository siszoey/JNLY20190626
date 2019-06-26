package com.lib.bandaid.widget.drag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.lib.bandaid.utils.MeasureScreen;

import java.lang.reflect.Field;

/**
 * Created by zy on 2017/5/22.
 */

public class CustomDrawerLayout extends DrawerLayout {

    private boolean isOpened;

    private Context context;

    private IListening iListening;

    public CustomDrawerLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        this.setDrawerListener(new SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isOpened = true;
                if (iListening != null) {
                    iListening.opened();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isOpened = false;
                if (iListening != null) {
                    iListening.closed();
                }
            }
        });
    }


    public void toggle() {
        if (isOpened) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        if (!isOpened) {
            try {
                openDrawer(Gravity.RIGHT);
                isOpened = true;
            } catch (Exception e) {
                openDrawer(Gravity.LEFT);
                isOpened = true;
            }
        }
    }

    public void close() {
        if (isOpened) {
            closeDrawers();
            isOpened = false;
        }
    }

    public void setIListening(IListening iListening) {
        this.iListening = iListening;
    }

    public interface IListening {
        public void opened();

        public void closed();
    }


    public void setMargin(float ratio) {
        try {
            // 去看源码就可以知道，mMinDrawerMargin 默认是64dp
            // 用反射来设置划动出来的距离 mMinDrawerMargin
            Field mMinDrawerMarginField = DrawerLayout.class.getDeclaredField("mMinDrawerMargin");
            mMinDrawerMarginField.setAccessible(true);
            int minDrawerMargin;
            if (MeasureScreen.isVertical(context)) {
                minDrawerMargin = (int) (MeasureScreen.getScreenWidth(context) * ratio);
            } else {
                minDrawerMargin = (int) (MeasureScreen.getScreenHeight(context) * 2 * ratio);
            }
            mMinDrawerMarginField.set(this, minDrawerMargin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置阴影颜色
        this.setScrimColor(Color.parseColor("#55000000"));
        // 设置边缘颜色
        this.setDrawerShadow(new ColorDrawable(Color.parseColor("#22000000")), Gravity.RIGHT);
    }
}
