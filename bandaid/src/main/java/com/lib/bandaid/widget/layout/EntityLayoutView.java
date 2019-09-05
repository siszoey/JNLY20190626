package com.lib.bandaid.widget.layout;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lib.bandaid.R;
import com.lib.bandaid.util.DateUtil;
import com.lib.bandaid.util.MeasureScreen;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.ReflectUtil;

import java.util.Date;
import java.util.Map;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by zy on 2019/6/3.
 */

public class EntityLayoutView<T> extends ScrollView {

    private Mode mode = Mode.READ_ONLY;
    private Context context;
    private LinearLayout layout;
    private T entity;

    //
    private int lineHigh = 45;

    private LinearLayout.LayoutParams params;
    private LinearLayout.LayoutParams lineParams;


    public EntityLayoutView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EntityLayoutView setData(T t) {
        this.entity = t;
        return this;
    }

    private void init() {
        this.lineHigh = MeasureScreen.dip2px(context, lineHigh);
        this.lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MeasureScreen.dip2px(context, 1));
        this.layout = new LinearLayout(context);
        this.layout.setOrientation(VERTICAL);
        this.addView(layout);
        setPadding(5, 5, 5, 5);
    }


    /**
     * 解析实体
     */
    public void resolutionData() {
        if (entity == null) return;
        View view;
        if (entity instanceof Map) {
            Map map = (Map) entity;
            int index = 0;
            for (Object key : map.keySet()) {
                if (mode == Mode.READ_ONLY) {
                    view = createViewItemReadOnly(key, map.get(key));
                } else {
                    view = createViewItemReadWrite(key, map.get(key));
                }
                if (index % 2 == 0) {
                    view.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
                } else {
                    view.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                index++;
                layout.addView(view);
                addLine();
            }
        } else {
            String[] array = ReflectUtil.columnArray(entity.getClass());
            for (int i = 0, len = array.length; i < len; i++) {
                if (mode == Mode.READ_ONLY) {
                    view = createViewItemReadOnly(array[i], ReflectUtil.getFieldValue(entity, array[i]));
                } else {
                    view = createViewItemReadWrite(array[i], ReflectUtil.getFieldValue(entity, array[i]));
                }
                if (i % 2 == 0) {
                    view.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
                } else {
                    view.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                layout.addView(view);
                addLine();
            }
        }
    }

    private void addLine() {
        View line = new View(context);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(Color.GRAY);
        layout.addView(line);
    }

    private View createViewItemReadOnly(Object field, Object val) {
        String name = "";
        if (field instanceof String) {
            name = (String) field;
        }

        LinearLayout item = new LinearLayout(context);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        item.setLayoutParams(params);
        item.setOrientation(HORIZONTAL);

        //key
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 5;
        TextView nameView = new TextView(context);
        nameView.setLayoutParams(params);
        nameView.setPadding(5, 0, 5, 0);
        nameView.setGravity(Gravity.RIGHT | Gravity.CENTER);
        nameView.setText(name + "：");
        item.addView(nameView);

        //val
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 2;
        TextView valueView = new TextView(context);
        valueView.setLayoutParams(params);
        valueView.setPadding(5, 0, 5, 0);
        valueView.setGravity(Gravity.LEFT | Gravity.CENTER);
        if (val instanceof Date) {
            valueView.setText(DateUtil.dateTimeToStr((Date) val));
        } else {
            valueView.setText(val == null ? "" : val.toString());
        }
        item.addView(valueView);
        return item;
    }

    private View createViewItemReadWrite(Object field, Object val) {

        String name = "";
        Class clazz = null;
        if (field instanceof String) {
            name = (String) field;
        }

        LinearLayout item = new LinearLayout(context);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        item.setLayoutParams(params);
        item.setOrientation(HORIZONTAL);

        //key
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 5;
        TextView nameView = new TextView(context);
        nameView.setLayoutParams(params);
        nameView.setPadding(5, 0, 5, 0);
        nameView.setGravity(Gravity.RIGHT | Gravity.CENTER);
        nameView.setText(name + "：");
        item.addView(nameView);
        //------------------------------------------------------------------------------------------
        //val
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 2;
        EditText valueView = new EditText(context);
        valueView.setLayoutParams(params);
        valueView.setBackground(null);
        valueView.setPadding(5, 0, 5, 0);
        valueView.setGravity(Gravity.LEFT | Gravity.CENTER);

        setEditText(clazz, valueView, val);

        valueView.setTag(name);
        item.addView(valueView);
        return item;
    }

    public static void setEditText(Class<?> field, EditText editText, Object val) {

        boolean isNumber = ObjectUtil.isNumber(field);
        boolean isDate = ObjectUtil.isDate(field);
        if (isNumber) editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (isDate) {
            editText.setText(DateUtil.dateTimeToStr((Date) val));
        } else {
            editText.setText(val == null ? "" : val.toString());
        }
    }

    public enum Mode {
        READ_ONLY,
        READ_WRITE
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
