package com.lib.bandaid.widget.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.lib.bandaid.R;

public class ToggleRadioButton extends AppCompatCheckBox {

    public ToggleRadioButton(Context context) {
        this(context, null);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.radioButtonStyle);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
        if (!isChecked()) {
            ViewParent group = getParent();
            if (group != null && group instanceof RadioGroup) {
                ((RadioGroup) group).clearCheck();
            }
        }
    }
}
