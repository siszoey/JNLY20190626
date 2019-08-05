package com.lib.bandaid.widget.easyui.ui_v1;

import android.content.Context;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.lib.bandaid.widget.edittext.ClearEditText;

public class ComplexTextView extends TextInputLayout {

    private final String CONSTANT_D = "°";
    private final String CONSTANT_F = "′";
    private final String CONSTANT_M = "″";


    //数字键盘
    private final String INPUT_TYPE_NUMBER = "number";
    //英文键盘
    private final String INPUT_TYPE_EMAIL = "english";


    private ClearEditText editText;

    private boolean inputAble = true;
    private String inputType;

    public ComplexTextView(Context context) {
        super(context);
        init();
    }

    public ComplexTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ComplexTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.editText = new ClearEditText(getContext());
        this.editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.editText.setBackground(null);
        this.editText.setPadding(0, 0, 0, 0);
        this.addView(editText);
    }

    public void setText(String text) {
        this.editText.setText(text);
    }

    public String getText() {
        return this.editText.getText().toString();
    }


    public EditText getEditText() {
        return editText;
    }

    public void setInputType(int type) {
        this.editText.setInputType(type);
    }

    public void setInputAble(boolean flag) {
        this.inputAble = flag;
    }


    public void setEditAble(boolean flag) {
        this.inputAble = flag;
        this.setEnabled(flag);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!inputAble) return true;
        return super.onInterceptTouchEvent(ev);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        this.editText.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        this.editText.removeTextChangedListener(watcher);
    }

    public void inputTypeNumber() {
        this.editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    public void inputTypeText() {
        this.editText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void inputTypeEnglish() {
        this.editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        this.editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }
}
