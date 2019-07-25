package com.lib.bandaid.widget.easyui.ui_v1;

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

import com.lib.bandaid.R;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.ImgUtil;
import com.lib.bandaid.utils.MeasureScreen;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.utils.ViewTreeUtil;
import com.lib.bandaid.utils.ViewUtil;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.lib.bandaid.widget.easyui.enums.Status;
import com.lib.bandaid.widget.easyui.ui.EventImageView;
import com.lib.bandaid.widget.easyui.ui.PropertyEditView;
import com.lib.bandaid.widget.easyui.ui.SimpleDialog;
import com.lib.bandaid.widget.easyui.utils.EasyUtil;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class PropertyView extends ScrollView {

    private Context context;
    private LinearLayout layout;
    private EasyUiXml entity;
    //
    private int lineHigh = 45;
    private LinearLayout.LayoutParams params;
    private LinearLayout.LayoutParams lineParams;
    //图片字段
    private String[] imgFields;


    public PropertyView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PropertyView setData(EasyUiXml entity) {
        this.entity = entity;
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
        UiXml uiXml;
        List<UiXml> list = entity.getUiXml();
        for (int i = 0, len = list.size(); i < len; i++) {
            uiXml = list.get(i);
            view = createViewItemWrite(uiXml);
            if (i % 2 == 0) {
                view.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
            } else {
                view.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
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

    private View createViewItemWrite(UiXml uiXml) {
        LinearLayout itemLine = createLineH();
        TextView nameView = createNameView(uiXml);
        itemLine.addView(nameView);

        View valueView;
        if (uiXml.isDialogSel()) {
            valueView = createTextView(uiXml);
            bindSimpleDialog((TextView) valueView, uiXml);
        } /*else if (uiXml.isMultiSel()) {
            valueView = createMultiSel(uiXml);
        } else if (uiXml.isSingleSel()) {
            valueView = createSingleSel(uiXml);
        } */ else if (uiXml.isImg()) {
            valueView = createImgView(uiXml);
        } else {
            valueView = createEditText(uiXml);
        }
        itemLine.addView(valueView);
        return itemLine;
    }

    private void setImg(UiXml uiXml, ImageView imageView) {
        String uri;
        if (imgAdapter == null) {
            uri = uiXml.getDisPlay() + "";
        } else {
            uri = imgAdapter.adapter(uiXml.getDisPlay());
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

    private void bindSimpleDialog(final TextView view, UiXml uiXml) {
        ViewUtil.setRightDrawable(view, R.drawable.ic_arrow_drop_down_black);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ItemXml> list = uiXml.getItemXml();
                List<Integer> sel = EasyUtil.label2Index(uiXml, view.getText().toString());
                SimpleDialog.newInstance(list, sel, uiXml.getStatus() == Status.multi, new BaseDialogFrg.ICallBack<List<ItemXml>>() {
                    @Override
                    public void callback(List<ItemXml> values) {
                        view.setText(EasyUtil.list2Label(values));
                    }
                }).show(context);
            }
        });
    }

    private TextView createNameView(UiXml uiXml) {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 5;
        TextView nameView = new TextView(context);
        nameView.setLayoutParams(params);
        nameView.setPadding(5, 0, 5, 0);
        nameView.setGravity(Gravity.RIGHT | Gravity.CENTER);
        String name = uiXml.getAlias();
        nameView.setText(name + "：");
        return nameView;
    }

    private RadioGroup createSingleSel(UiXml uiXml) {
        List<ItemXml> codedValues = uiXml.getItemXml();
        RadioGroup radioGroup = new RadioGroup(context);
        uiXml.setView(radioGroup);
        RadioButton radioButton;
        ItemXml itemXml;
        for (int i = 0; i < codedValues.size(); i++) {
            itemXml = codedValues.get(i);
            radioButton = new RadioButton(context);
            radioButton.setText(itemXml.getValue());
            radioGroup.addView(radioButton);
        }
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 2;
        radioGroup.setLayoutParams(params);
        return radioGroup;
    }

    private LinearLayout createMultiSel(UiXml uiXml) {
        List<ItemXml> codedValues = uiXml.getItemXml();
        LinearLayout lineV = createLineV();
        uiXml.setView(lineV);
        CheckBox checkBox;
        ItemXml itemXml;
        for (int i = 0; i < codedValues.size(); i++) {
            itemXml = codedValues.get(i);
            checkBox = new CheckBox(context);
            checkBox.setText(itemXml.getValue());
            lineV.addView(checkBox);
        }
        params = (LinearLayout.LayoutParams) lineV.getLayoutParams();
        params.weight = 2;
        lineV.setLayoutParams(params);
        return lineV;
    }

    private EventImageView createImgView(UiXml uiXml) {
        EventImageView imageView = new EventImageView(context);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 2;
        imageView.setLayoutParams(params);
        setImg(uiXml, imageView);
        return imageView;
    }

    private TextView createTextView(UiXml uiXml) {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 2;
        TextView textView = new TextView(context);
        textView.setLayoutParams(params);
        textView.setBackground(null);
        textView.setPadding(5, 0, 5, 0);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER);
        setEditText(textView, uiXml);
        return textView;
    }

    private EditText createEditText(UiXml uiXml) {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHigh);
        params.weight = 2;
        EditText editText = new EditText(context);
        editText.setLayoutParams(params);
        editText.setBackground(null);
        editText.setPadding(5, 0, 5, 0);
        editText.setGravity(Gravity.LEFT | Gravity.CENTER);
        setEditText(editText, uiXml);
        return editText;
    }

    private void setEditText(TextView editText, UiXml uiXml) {
        uiXml.setView(editText);
        boolean isNumber = uiXml.isNumber();
        boolean isDate = uiXml.isDate();
        boolean isReadOnly = uiXml.getReadOnly();
        if (isNumber) editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Object val = uiXml.getDisPlay();
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
        if (isReadOnly) {
            editText.setFocusable(false);
        }
    }


    private PropertyEditView.InputFace inputFace;

    public interface InputFace {
        public void input(View view);
    }

    private PropertyEditView.ImgAdapter imgAdapter;

    public interface ImgAdapter {
        public String adapter(Object val);
    }

    public void setInputFace(PropertyEditView.InputFace inputFace) {
        this.inputFace = inputFace;
    }

    public void setImgAdapter(PropertyEditView.ImgAdapter imgAdapter) {
        this.imgAdapter = imgAdapter;
    }

    /**
     * 获取保存时的key view
     *
     * @return
     */
    public Map getForm() {
        Map<String, Object> map = entity.getFormMap();
        return map;
    }


    public UiXml getUiXmlByKey(String key) {
        return EasyUtil.findUiXmlByKey(entity, key);
    }

    public View getViewByKey(String key) {
        UiXml uiXml = EasyUtil.findUiXmlByKey(entity, key);
        if (uiXml == null) return null;
        return uiXml.getView();
    }
}

