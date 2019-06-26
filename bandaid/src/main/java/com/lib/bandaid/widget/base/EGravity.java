package com.lib.bandaid.widget.base;

import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by zy on 2018/12/10.
 */

public enum EGravity {

    RIGHT("RIGHT", 5),//
    LEFT("LEFT", 3),
    TOP("TOP", 48),
    CENTER("CENTER", 17),
    BOTTOM("BOTTOM", 80),
    RIGHT_TOP("RIGHT_TOP", 53),
    RIGHT_CENTER("RIGHT_CENTER", 21),
    RIGHT_BOTTOM("RIGHT_BOTTOM", 85),
    LEFT_TOP("LEFT_TOP", 51),
    LEFT_CENTER("LEFT_CENTER", 19),
    LEFT_BOTTOM("LEFT_BOTTOM", 83),
    TOP_CENTER("TOP_CENTER", 49),
    BOTTOM_CENTER("BOTTOM_CENTER", 81);

    private String name;
    private int value;

    EGravity(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 根据枚举描名称获取枚举对象
     */
    public static EGravity getEnumByName(String name) {
        EGravity type = null;
        for (int i = 0; i < EGravity.values().length; i++) {
            if (EGravity.values()[i].toString().equalsIgnoreCase(name.trim())) {
                type = EGravity.values()[i];
                break;
            }
        }
        return type;
    }

    /**
     * 根据枚举整型表达式获取枚举对象
     */
    public static EGravity getEnumByValue(int value) {
        EGravity type = null;
        for (int i = 0; i < EGravity.values().length; i++) {
            if (EGravity.values()[i].getValue() == value) {
                type = EGravity.values()[i];
                break;
            }
        }
        return type;
    }


    public static ViewGroup.LayoutParams initMapWidgetParam(int gravity, RelativeLayout.LayoutParams params) {
        EGravity widgetGravity = EGravity.getEnumByValue(gravity);
        if (params == null) return null;
        if (widgetGravity == EGravity.TOP || widgetGravity == EGravity.LEFT_TOP) {
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (widgetGravity == EGravity.BOTTOM || widgetGravity == EGravity.LEFT_BOTTOM) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (widgetGravity == EGravity.RIGHT_TOP || widgetGravity == EGravity.RIGHT) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else if (widgetGravity == EGravity.RIGHT_BOTTOM) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (widgetGravity == EGravity.CENTER) {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (widgetGravity == EGravity.LEFT_CENTER) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (widgetGravity == EGravity.TOP_CENTER) {
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (widgetGravity == EGravity.BOTTOM_CENTER) {
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (widgetGravity == EGravity.RIGHT_CENTER) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        return params;
    }
}
