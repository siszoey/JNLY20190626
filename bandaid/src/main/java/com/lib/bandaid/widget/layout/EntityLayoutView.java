package com.lib.bandaid.widget.layout;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lib.bandaid.R;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.ReflectUtil;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by zy on 2019/6/3.
 */

public class EntityLayoutView<T> extends ScrollView {

    Context context;
    LinearLayout layout;
    T entity;

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
        if (entity instanceof Map) {
            Map map = (Map) entity;
            for (Object key : map.keySet()) {
                layout.addView(createViewItem(key.toString(), map.get(key)));
            }
        } else {
            String[] array = ReflectUtil.columnArray(entity.getClass());
            for (int i = 0, len = array.length; i < len; i++) {
                layout.addView(createViewItem(array[i], ReflectUtil.getFieldValue(entity, array[i])));
            }
        }

    }


    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    private View createViewItem(String name, Object val) {
        System.out.println(name + ":" + val);
        LinearLayout item = new LinearLayout(context);
        item.setLayoutParams(params);
        item.setOrientation(HORIZONTAL);
        params.weight = 1;
        TextView nameView = new TextView(context);
        nameView.setLayoutParams(params);
        nameView.setPadding(5, 5, 5, 5);
        nameView.setGravity(Gravity.RIGHT | Gravity.CENTER);

        params.weight = 3;
        TextView valueView = new TextView(context);
        valueView.setLayoutParams(params);
        valueView.setPadding(5, 5, 5, 5);
        valueView.setGravity(Gravity.LEFT | Gravity.CENTER);


        nameView.setText(name + "：");

        if (val instanceof GregorianCalendar) {
            Date date = ((GregorianCalendar) val).getTime();
            valueView.setText(DateUtil.dateTimeToStr(date));
        } else {
            valueView.setText(val == null ? "" : val.toString());
        }
        item.addView(nameView);
        item.addView(valueView);
        return item;
    }

}
