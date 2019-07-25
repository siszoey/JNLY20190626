package com.lib.bandaid.widget.easyui.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Domain;
import com.esri.arcgisruntime.data.Field;
import com.lib.bandaid.R;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.ImgUtil;
import com.lib.bandaid.utils.MeasureScreen;
import com.lib.bandaid.utils.NumberUtil;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.utils.ViewTreeUtil;
import com.lib.bandaid.utils.ViewUtil;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;

import java.util.ArrayList;
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

public class PropertyEditView extends ScrollView {

    private Context context;
    private LinearLayout layout;
    private Map<Field, Object> entity;
    //
    private int lineHigh = 45;
    private LinearLayout.LayoutParams params;
    private LinearLayout.LayoutParams lineParams;
    //图片字段
    private String[] imgFields;


    public PropertyEditView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PropertyEditView setData(Map<Field, Object> t) {
        this.entity = t;
        return this;
    }

    public void setImgFields(String... imgFields) {
        this.imgFields = imgFields;
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
        for (Field field : entity.keySet()) {
            view = createViewItemWrite(field, entity.get(field));
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

    private View createViewItemWrite(Field field, Object val) {
        LinearLayout itemLine = createLineH();
        TextView nameView = createNameView(field);
        itemLine.addView(nameView);

        View valueView;
        if (isDialogSel(field)) {
            valueView = createTextView(field, val);
            bindSimpleDialog((TextView) valueView, field, val);
        } else if (isSingleSel(field)) {
            //valueView = createSingleSel(field, val);
            valueView = createMultiSel(field, val);
        } else if (isImg(field)) {
            valueView = createImgView(field, val);
        } else {
            valueView = createEditText(field, val);
        }
        itemLine.addView(valueView);
        return itemLine;
    }

    private void setImg(Field field, ImageView imageView, Object val) {
        String uri;
        if (imgAdapter == null) {
            uri = val + "";
        } else {
            uri = imgAdapter.adapter(val);
        }
        ImgUtil.simpleLoadImg(imageView, uri);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputFace != null) {
                    inputFace.input(v);
                }
            }
        });
    }

    //----------------------------------------------------------------------------------------------

    private LinearLayout createLineH() {
        LinearLayout itemLine = new LinearLayout(context);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        itemLine.setLayoutParams(params);
        itemLine.setOrientation(HORIZONTAL);
        return itemLine;
    }

    private LinearLayout createLineV() {
        LinearLayout itemLine = new LinearLayout(context);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        itemLine.setLayoutParams(params);
        itemLine.setOrientation(VERTICAL);
        return itemLine;
    }

    private void bindSimpleDialog(final TextView view, Field field, Object val) {
        ViewUtil.setRightDrawable(view, R.drawable.ic_arrow_drop_down_black);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CodedValue> list = getCodedValues(field);
                List<Integer> sel = label2List(field, view.getText().toString());
                /*SimpleDialog.newInstance(list, sel, false, new BaseDialogFrg.ICallBack<List<CodedValue>>() {
                    @Override
                    public void callback(List<CodedValue> values) {
                        view.setText(list2Label(values));
                    }
                }).show(context);*/
            }
        });
    }

    private TextView createNameView(Field field) {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 5;
        TextView nameView = new TextView(context);
        nameView.setLayoutParams(params);
        nameView.setPadding(5, 0, 5, 0);
        nameView.setGravity(Gravity.RIGHT | Gravity.CENTER);
        String name = field.getAlias();
        nameView.setText(name + "：");
        return nameView;
    }

    private RadioGroup createSingleSel(Field field, Object val) {
        List<CodedValue> codedValues = getCodedValues(field);
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setTag(field);
        RadioButton radioButton;
        for (int i = 0; i < codedValues.size(); i++) {
            radioButton = new RadioButton(context);
            radioButton.setText(codedValues.get(i).getName());
            radioButton.setTag(codedValues.get(i));
            radioGroup.addView(radioButton);
        }
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 2;
        radioGroup.setLayoutParams(params);
        return radioGroup;
    }

    private LinearLayout createMultiSel(Field field, Object val) {
        List<CodedValue> codedValues = getCodedValues(field);
        LinearLayout lineV = createLineV();
        lineV.setTag(field);
        CheckBox checkBox;
        for (int i = 0; i < codedValues.size(); i++) {
            checkBox = new CheckBox(context);
            checkBox.setText(codedValues.get(i).getName());
            checkBox.setTag(codedValues.get(i));
            lineV.addView(checkBox);
        }
        params = (LinearLayout.LayoutParams) lineV.getLayoutParams();
        params.weight = 2;
        lineV.setLayoutParams(params);
        return lineV;
    }

    private EventImageView createImgView(Field field, Object val) {
        EventImageView imageView = new EventImageView(context);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 2;
        imageView.setLayoutParams(params);
        setImg(field, imageView, val);
        return imageView;
    }

    private TextView createTextView(Field field, Object val) {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 2;
        TextView textView = new TextView(context);
        textView.setLayoutParams(params);
        textView.setBackground(null);
        textView.setPadding(5, 0, 5, 0);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER);
        textView.setTag(field);
        setEditText(field, textView, val);
        return textView;
    }

    private EditText createEditText(Field field, Object val) {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 2;
        EditText editText = new EditText(context);
        editText.setLayoutParams(params);
        editText.setBackground(null);
        editText.setPadding(5, 0, 5, 0);
        editText.setGravity(Gravity.LEFT | Gravity.CENTER);
        editText.setTag(field);
        setEditText(field, editText, val);
        return editText;
    }

    private void setEditText(Field field, TextView editText, Object val) {
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


    private InputFace inputFace;

    public interface InputFace {
        public void input(View view);
    }

    private ImgAdapter imgAdapter;

    public interface ImgAdapter {
        public String adapter(Object val);
    }

    public void setInputFace(InputFace inputFace) {
        this.inputFace = inputFace;
    }

    public void setImgAdapter(ImgAdapter imgAdapter) {
        this.imgAdapter = imgAdapter;
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

    private void createImg(Field field, Object val) {
        Domain domain = field.getDomain();
        CodedValueDomain codedValueDomain;
        List<CodedValue> codedValues = null;
        if (domain != null && domain instanceof CodedValueDomain) {
            codedValueDomain = (CodedValueDomain) domain;
            if (codedValueDomain != null) {
                codedValues = codedValueDomain.getCodedValues();
                System.out.println(codedValues);
                for (CodedValue value : codedValues) {
                    System.out.println(value.getCode() + ":" + value.getName());
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------

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

    private boolean isImg(Field field) {
        if (field == null) return false;
        if (imgFields == null) return false;
        String name = field.getName();
        for (String imgField : imgFields) {
            if (name.equals(imgField)) return true;
        }
        return false;
    }

    private boolean isDialogSel(Field field) {
        List<CodedValue> codedValues = getCodedValues(field);
        if (codedValues == null || codedValues.size() == 0) return false;
        return true;
    }

    private boolean isSingleSel(Field field) {
        List<CodedValue> codedValues = getCodedValues(field);
        if (codedValues == null || codedValues.size() == 0) return false;
        return true;
    }

    private boolean isMultiSel(Field field) {
        List<CodedValue> codedValues = getCodedValues(field);
        if (codedValues == null || codedValues.size() == 0) return false;
        return true;
    }

    private List<CodedValue> getCodedValues(Field field) {
        Domain domain = field.getDomain();
        CodedValueDomain codedValueDomain;
        List<CodedValue> codedValues = null;
        if (domain != null && domain instanceof CodedValueDomain) {
            codedValueDomain = (CodedValueDomain) domain;
            if (codedValueDomain != null) {
                codedValues = codedValueDomain.getCodedValues();
                return codedValues;
            }
        }
        return null;
    }

    private String list2Label(List<CodedValue> values) {
        if (values == null) return null;
        String label = "";
        CodedValue value;
        for (int i = 0; i < values.size(); i++) {
            value = values.get(i);
            label += value.getName();
            if (i != values.size() - 1) label += ";";
        }
        return label;
    }

    private List<Integer> label2List(Field field, String labels) {
        String[] array = labels.split(";");
        if (array == null) return null;
        List<Integer> res = new ArrayList<>();
        List<CodedValue> list = getCodedValues(field);
        CodedValue value;
        for (String label : array) {
            if (label == null) continue;
            for (int j = 0; j < list.size(); j++) {
                value = list.get(j);
                if (value == null) continue;
                if (label.equals(value.getName())) {
                    if (!res.contains(j)) res.add(j);
                }
            }
        }
        return res;
    }
}
