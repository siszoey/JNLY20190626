package com.lib.bandaid.widget.easyui.ui_v1;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lib.bandaid.R;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.ImgUtil;
import com.lib.bandaid.utils.MeasureScreen;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.utils.ViewUtil;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.lib.bandaid.widget.easyui.enums.InputType;
import com.lib.bandaid.widget.easyui.enums.ItemFrom;
import com.lib.bandaid.widget.easyui.enums.Status;
import com.lib.bandaid.widget.easyui.ui.EventImageView;
import com.lib.bandaid.widget.easyui.ui.SimpleDialog;
import com.lib.bandaid.widget.easyui.utils.EasyUtil;
import com.lib.bandaid.widget.easyui.utils.RegexUtil;
import com.lib.bandaid.widget.easyui.utils.WidgetUtil;
import com.lib.bandaid.widget.easyui.xml.EasyUiXml;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.easyui.xml.UiXml;
import com.lib.bandaid.widget.easyui.xml.VerifyXml;
import com.lib.bandaid.widget.text.SimpleTextWatch;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;


public class PropertyView extends ScrollView {

    private Context context;
    private LinearLayout layout;
    private EasyUiXml entity;
    //
    private int lineHigh = 45;
    private LinearLayout.LayoutParams params;

    private ILifeCycle lifeCycle;

    public PropertyView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        this.lineHigh = MeasureScreen.dip2px(context, lineHigh);
        this.layout = new LinearLayout(context);
        this.layout.setOrientation(LinearLayout.VERTICAL);
        this.addView(layout);
        setPadding(5, 5, 5, 5);
    }

    public PropertyView setListener(ILifeCycle listener) {
        this.lifeCycle = listener;
        return this;
    }

    /**
     * 解析实体
     */
    public void resolutionData(EasyUiXml entity) {
        if (lifeCycle != null) lifeCycle.beforeCreate();
        this.entity = entity;
        if (entity == null) return;
        View view;
        List<UiXml> list = entity.getUiXml();
        int index = 0;
        for (UiXml uiXml : list) {
            view = createViewItemWrite(uiXml);
            if (index % 2 == 0) {
                WidgetUtil.setViewBackGColor(view, R.color.sky_blue);
            } else {
                WidgetUtil.setViewBackGColor(view, R.color.white);
            }
            layout.addView(view);
            if (!uiXml.getVisible()) {
                view.setVisibility(GONE);
                continue;
            }
            layout.addView(WidgetUtil.createHLine(context));
            index++;
        }
        if (lifeCycle != null) lifeCycle.afterCreate();
    }

    private View createViewItemWrite(UiXml uiXml) {
        LinearLayout itemLine = WidgetUtil.createLineH(context);
        TextView nameView = createNameView(uiXml);
        itemLine.addView(nameView);

        View valueView;
        if (uiXml.isDialogSel()) {
            valueView = createComplexTextView(uiXml);
            bindSimpleDialog((ComplexTextView) valueView, uiXml);
        } else if (uiXml.isImg()) {
            valueView = createImgView(uiXml);
        } else {
            valueView = createComplexTextView(uiXml);
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
    private void bindSimpleDialog(final ComplexTextView view, UiXml uiXml) {
        view.setInputAble(false);
        ViewUtil.setRightDrawable(view, R.drawable.ic_arrow_drop_down_black);
        boolean isReadOnly = uiXml.getReadonly();
        if (isReadOnly) return;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ItemXml> list;
                if (uiXml.getItemFrom() == ItemFrom.xml) {
                    list = uiXml.getItemXml();
                } else {
                    if (listAdapter != null) listAdapter.listAdapter(uiXml);
                    list = uiXml.getItemXml();
                }
                if (list == null) return;
                List<Integer> sel = EasyUtil.label2Index(uiXml, view.getText());
                SimpleDialog.newInstance(list, sel, uiXml.getStatus() == Status.multi, new BaseDialogFrg.ICallBack<List<ItemXml>>() {
                    @Override
                    public void callback(List<ItemXml> values) {
                        uiXml.setLabel(EasyUtil.list2Label(values));
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

    private EventImageView createImgView(UiXml uiXml) {
        EventImageView imageView = new EventImageView(context);
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 2;
        imageView.setLayoutParams(params);
        uiXml.setView(imageView);
        setImg(uiXml, imageView);
        return imageView;
    }


    private ComplexTextView createComplexTextView(UiXml uiXml) {
        params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 2;
        ComplexTextView view = WidgetUtil.complexTextView(context, null);
        view.setLayoutParams(params);
        setComplexTextView(view, uiXml);
        return view;
    }

    private void setComplexTextView(ComplexTextView complexTextView, UiXml uiXml) {
        uiXml.setView(complexTextView);
        boolean isNumber = uiXml.isNumber();
        boolean isDate = uiXml.isDate();
        boolean readOnly = uiXml.getReadonly();
        InputType inputType = uiXml.getInputType();
        if (inputType == null) {
            if (isNumber) complexTextView.inputTypeNumber();
        } else {
            if (inputType == InputType.numPrior) {
                complexTextView.inputTypeNumberPrior();
            }
            if (inputType == InputType.english) {
                complexTextView.inputTypeEnglish();
            }
            if (inputType == InputType.text) {
                complexTextView.inputTypeText();
            }
            if (inputType == InputType.number) {
                complexTextView.inputTypeNumber();
            }
        }
        if (readOnly) complexTextView.setEditAble(false);
        Object val = uiXml.getDisPlay();
        {
            VerifyXml verifyXml = uiXml.getVerifyXml();
            if (verifyXml != null) {
                //数据验证
                complexTextView.addTextChangedListener(
                        new SimpleTextWatch(
                                uiXml.getTextBefore() == null ? new SimpleTextWatch.IBefore() {
                                    @Override
                                    public void before(CharSequence s, int start, int count, int after) {

                                    }
                                } : uiXml.getTextBefore(),
                                uiXml.getOnChange(),
                                uiXml.getTextAfter() == null ? new SimpleTextWatch.IAfter() {
                                    @Override
                                    public void after(String s) {
                                        if (verifyXml.getCanNull() && StringUtil.isEmpty(s)) return;
                                        boolean verify = RegexUtil.match(verifyXml.getRegex(), s);
                                        if (verify) complexTextView.setError(null);
                                        else complexTextView.setError(verifyXml.getMsg());
                                    }
                                } : uiXml.getTextAfter()));
            }
        }
        if (isDate) {
            if (val != null) {
                Date date = ((GregorianCalendar) val).getTime();
                complexTextView.setText(DateUtil.dateTimeToStr(date));
            }
            if (readOnly) return;
            complexTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputFace != null) {
                        inputFace.input(v);
                    }
                }
            });
        } else {
            if (StringUtil.isEmpty(val)) return;
            complexTextView.setText(val.toString());
        }
    }

    public boolean verifyForm() {
        return entity.verifyForm();
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


    private InputFace inputFace;
    private ImgAdapter imgAdapter;
    private ListAdapter listAdapter;

    public interface InputFace {
        public void input(View view);
    }


    public interface ImgAdapter {
        public String adapter(Object val);
    }

    public interface ListAdapter {
        public void listAdapter(UiXml uiXml);
    }

    public void setInputFace(InputFace inputFace) {
        this.inputFace = inputFace;
    }

    public void setImgAdapter(ImgAdapter imgAdapter) {
        this.imgAdapter = imgAdapter;
    }

    public void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    //----------------------------------------------------------------------------------------------

    public UiXml getUiXmlByKey(String key) {
        return EasyUtil.findUiXmlByKey(entity, key);
    }

    public <T extends View> T getViewByKey(String key) {
        UiXml uiXml = EasyUtil.findUiXmlByKey(entity, key);
        if (uiXml == null) return null;
        return (T) uiXml.getView();
    }

    public <T extends View> T getViewByAlias(String alias) {
        UiXml uiXml = EasyUtil.findUiXmlByAlias(entity, alias);
        if (uiXml == null) return null;
        return (T) uiXml.getView();
    }
}

