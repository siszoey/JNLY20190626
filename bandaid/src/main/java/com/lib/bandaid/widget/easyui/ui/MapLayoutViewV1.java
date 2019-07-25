package com.lib.bandaid.widget.easyui.ui;

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

import com.esri.arcgisruntime.data.Field;
import com.lib.bandaid.R;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.MeasureScreen;
import com.lib.bandaid.utils.NumberUtil;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.utils.ViewTreeUtil;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by zy on 2019/6/3.
 * arcgis 属性布局
 */

public class MapLayoutViewV1 extends ScrollView {

    private Mode mode = Mode.READ_ONLY;
    private Context context;
    private LinearLayout layout;
    private Map<Field, Object> entity;

    //
    private int lineHigh = 45;
    private LinearLayout.LayoutParams params;
    private LinearLayout.LayoutParams lineParams;


    public MapLayoutViewV1(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MapLayoutViewV1 setData(Map<Field, Object> t) {
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
        int index = 0;
        for (Field key : entity.keySet()) {
            if (mode == Mode.READ_ONLY) {
                view = createViewItemReadOnly(key, entity.get(key));
            } else {
                view = createViewItemReadWrite(key, entity.get(key));
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
    }

    private void addLine() {
        View line = new View(context);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(Color.GRAY);
        layout.addView(line);
    }

    private View createViewItemReadOnly(Field field, Object val) {
        String name = field.getAlias();
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
        if (val instanceof GregorianCalendar) {
            Date date = ((GregorianCalendar) val).getTime();
            valueView.setText(DateUtil.dateTimeToStr(date));
        } else {
            valueView.setText(val == null ? "" : val.toString());
        }
        item.addView(valueView);
        return item;
    }

    private View createViewItemReadWrite(Field field, Object val) {
        String name = field.getAlias();
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
        setEditText(field, valueView, val);
        item.addView(valueView);
        return item;
    }

    private void setEditText(Field field, EditText editText, Object val) {
        editText.setTag(field);
        boolean isNumber = isNumber(field);
        boolean isDate = isDate(field);
        boolean isObjId = isObject(field);
        if (isNumber) editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (isDate) {
            if (val != null) {
                Date date = ((GregorianCalendar) val).getTime();
                editText.setText(DateUtil.dateTimeToStr(date));
            }
            editText.setFocusable(false);
            editText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputFace != null) {
                        inputFace.input(v);
                    }
                }
            });

        } else {
            editText.setText(val == null ? "" : val.toString());
        }
        if (isObjId) {
            editText.setFocusable(false);
        }
    }

    public enum Mode {
        READ_ONLY,
        READ_WRITE
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    private boolean isNumber(Field field) {
        if (field == null) return false;
        if (field.getFieldType() == Field.Type.DOUBLE
                || field.getFieldType() == Field.Type.FLOAT
                || field.getFieldType() == Field.Type.INTEGER
                || field.getFieldType() == Field.Type.SHORT) {
            return true;
        }
        return false;
    }

    private boolean isDate(Field field) {
        if (field == null) return false;
        if (field.getFieldType() == Field.Type.DATE) {
            return true;
        }
        return false;
    }

    private boolean isObject(Field field) {
        if (field == null) return false;
        if (field.getFieldType() == Field.Type.OID) {
            return true;
        }
        return false;
    }

    private InputFace inputFace;

    public interface InputFace {
        public void input(View view);
    }

    public void setInputFace(InputFace inputFace) {
        this.inputFace = inputFace;
    }

    public Map getForm() {
        List<EditText> editTexts = ViewTreeUtil.findTUnNullTag(this, EditText.class);
        Map map = new HashMap();
        EditText editText;
        Field field;
        Object val;
        for (int i = 0, count = editTexts.size(); i < count; i++) {
            editText = editTexts.get(i);
            field = (Field) editText.getTag();
            val = StringUtil.removeEmpty(editText.getText().toString());
            val = convertRunTimeObj(field, val);
            if (val == null) continue;
            map.put(field.getName(), val);
        }
        return map;
    }

    private Object convertRunTimeObj(Field field, Object val) {
        Object res = null;
        if (val == null) return null;
        String text = val.toString();
        if (field.getFieldType() == Field.Type.TEXT) {
            res = text;
        }
        if (field.getFieldType() == Field.Type.FLOAT) {
            res = NumberUtil.try2Float(text);
        }
        if (field.getFieldType() == Field.Type.DOUBLE) {
            res = NumberUtil.try2Double(text);
        }
        if (field.getFieldType() == Field.Type.SHORT) {
            res = NumberUtil.try2Short(text);
        }
        if (field.getFieldType() == Field.Type.INTEGER) {
            res = NumberUtil.try2Integer(text);
        }
        if (field.getFieldType() == Field.Type.DATE) {
            Date date = DateUtil.strToDateTime(text);
            if (date == null) {
                res = null;
            } else {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                res = calendar;
            }
        }
        if (field.getFieldType() == Field.Type.OID) {
            res = NumberUtil.try2Long(text);
        }
        return res;
    }
}
