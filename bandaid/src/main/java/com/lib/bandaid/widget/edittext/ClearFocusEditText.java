package com.lib.bandaid.widget.edittext;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by zy on 2017/7/3.
 * 拦截键盘向下的 EditTextView
 */

public class ClearFocusEditText extends AppCompatEditText {

    public ClearFocusEditText(Context context) {
        super(context);
    }

    public ClearFocusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearFocusEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            Log.v("xpf", "--------------------+onKeyDown---------软键盘弹出");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.v("xpf", "--------------------+onKeyPreIme---------软键盘退出");
        }
        return super.onKeyPreIme(keyCode, event);
    }
}

