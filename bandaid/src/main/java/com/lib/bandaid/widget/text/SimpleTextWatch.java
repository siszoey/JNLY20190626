package com.lib.bandaid.widget.text;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by zy on 2019/5/15.
 */

public class SimpleTextWatch implements TextWatcher {

    private IBefore iBefore;
    private IOnChange iOnChange;
    private IAfter iAfter;

    public SimpleTextWatch(IBefore iBefore) {
        this.iBefore = iBefore;
    }

    public SimpleTextWatch(IOnChange iOnChange) {
        this.iOnChange = iOnChange;
    }

    public SimpleTextWatch(IAfter iAfter) {
        this.iAfter = iAfter;
    }

    public SimpleTextWatch(IBefore iBefore, IOnChange iOnChange, IAfter iAfter) {
        this.iBefore = iBefore;
        this.iOnChange = iOnChange;
        this.iAfter = iAfter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (iBefore != null) iBefore.before(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (iOnChange != null) iOnChange.onChange(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (iAfter != null) iAfter.after(s.toString());
    }

    public interface IBefore {
        public void before(CharSequence s, int start, int count, int after);
    }

    public interface IOnChange {
        public void onChange(CharSequence s, int start, int before, int count);
    }

    public interface IAfter {
        public void after(String s);
    }
}
