package com.lib.bandaid.widget.easyui.ui_v1;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.widget.easyui.utils.RegexUtil;
import com.lib.bandaid.widget.easyui.xml.VerifyXml;
import com.lib.bandaid.widget.edittext.ClearEditText;
import com.lib.bandaid.widget.text.SimpleTextWatch;

public class ComplexTextView extends TextInputLayout {

    //数字键盘
    private final String INPUT_TYPE_NUMBER = "number";
    //英文键盘
    private final String INPUT_TYPE_EMAIL = "english";


    private ClearEditText editText;

    private boolean inputAble = true;

    private String inputType;

    private VerifyXml verifyXml;

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

    public VerifyXml getVerifyXml() {
        return verifyXml;
    }

    public void setVerifyXml(VerifyXml verifyXml) {
        this.verifyXml = verifyXml;
        if (verifyXml != null) {
            //数据验证
            editText.addTextChangedListener(
                    new SimpleTextWatch(
                            new SimpleTextWatch.IAfter() {
                                @Override
                                public void after(String s) {
                                    if (verifyXml.getCanNull() && StringUtil.isEmpty(s)) return;
                                    boolean verify = RegexUtil.match(verifyXml.getRegex(), s);
                                    if (verify) setError(null);
                                    else setError(verifyXml.getMsg());

                                    if (iListenChange != null && trigger)
                                        iListenChange.textChange(ComplexTextView.this.getId(), s.toString());
                                }
                            }));
        }
    }

    public boolean checkVerify() {
        String text = editText.getText().toString();
        if (verifyXml == null) return true;
        if (verifyXml.getCanNull() && StringUtil.isEmpty(text)) return true;
        boolean verify = RegexUtil.match(verifyXml.getRegex(), text);
        if (verify) setError(null);
        else setError(verifyXml.getMsg());
        return verify;
    }

    public void setText(String text) {
        this.editText.setText(text);
    }

    boolean trigger = true;

    public void setText(Object text, boolean trigger) {
        if (getEditText() == null) return;
        this.trigger = trigger;
        getEditText().setText(ObjectUtil.removeNull(text));
        this.trigger = true;
    }

    public void keepCursorLast() {
        Editable editable = editText.getText();
        Selection.setSelection(editable, editable.length());
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

    public void inputTypeNumberPrior() {
        this.editText.setRawInputType(Configuration.KEYBOARD_QWERTY);
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

    IListenChange iListenChange;

    public interface IListenChange {
        public void textChange(int id, String text);
    }

    public void setListenChange(IListenChange iListenChange) {
        this.iListenChange = iListenChange;
    }
}
