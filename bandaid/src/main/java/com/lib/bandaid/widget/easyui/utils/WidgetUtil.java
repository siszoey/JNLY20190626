package com.lib.bandaid.widget.easyui.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.lib.bandaid.util.MeasureScreen;
import com.lib.bandaid.widget.easyui.ui_v1.ComplexTextView;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public final class WidgetUtil {


    public static LinearLayout createLineH(Context context) {
        LinearLayout itemLine = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        itemLine.setLayoutParams(params);
        itemLine.setOrientation(HORIZONTAL);
        return itemLine;
    }

    public static LinearLayout createLineV(Context context) {
        LinearLayout itemLine = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        itemLine.setLayoutParams(params);
        itemLine.setOrientation(VERTICAL);
        return itemLine;
    }

    public static View createHLine(Context context) {
        View line = new View(context);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureScreen.dip2px(context, 1));
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(Color.GRAY);
        return line;
    }


    public static <T> T complexTextView(Context context, String error) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        ComplexTextView complexTextView = new ComplexTextView(context);
        complexTextView.setLayoutParams(params);
        complexTextView.setError(error);
        complexTextView.setErrorEnabled(false);
        return (T) complexTextView;
    }


    /**
     * 设置数字类型
     *
     * @param view
     */
    public static void setViewInputNum(TextView view) {
        view.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    public static void setViewInputNum(ComplexTextView view) {
        view.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }


    /**
     * 设置背景色
     *
     * @param view
     * @param color
     */
    public static void setViewBackGColor(View view, @ColorRes int color) {
        Context context = view.getContext();
        view.setBackgroundColor(context.getResources().getColor(color));
    }

    /**
     * 设置textView值监听
     *
     * @param view
     * @param iChangeLister
     */
    public static void setViewTextChangeLister(ComplexTextView view, IChangeLister iChangeLister) {
        if (view == null) return;
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (iChangeLister != null) iChangeLister.changeLister(s.toString());
            }
        });
    }

    /**
     * 设置textView值监听
     *
     * @param view
     * @param iChangeLister
     */
    public static void setViewTextChangeLister(TextView view, IChangeLister iChangeLister) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (iChangeLister != null) iChangeLister.changeLister(s.toString());
            }
        });
    }

    public interface IChangeLister {
        public void changeLister(String text);
    }
}
