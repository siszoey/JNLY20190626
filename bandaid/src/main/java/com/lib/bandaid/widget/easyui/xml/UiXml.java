package com.lib.bandaid.widget.easyui.xml;

import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.bandaid.utils.NumberUtil;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.widget.easyui.enums.InputType;
import com.lib.bandaid.widget.easyui.enums.ItemFrom;
import com.lib.bandaid.widget.easyui.enums.Status;
import com.lib.bandaid.widget.easyui.enums.UiType;
import com.lib.bandaid.widget.easyui.ui.EventImageView;
import com.lib.bandaid.widget.easyui.ui_v1.ComplexTextView;
import com.lib.bandaid.widget.easyui.utils.EasyUtil;
import com.lib.bandaid.widget.easyui.utils.RegexUtil;
import com.lib.bandaid.widget.easyui.utils.WidgetUtil;
import com.lib.bandaid.widget.text.SimpleTextWatch;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2019/7/21.
 */
@XStreamAlias("UiXml")
public class UiXml implements Serializable {

    /**
     * ui代码
     */
    @XStreamAlias("code")
    private String code;

    @XStreamAlias("alias")
    private String alias;

    @XStreamAlias("value")
    private Object value;
    /**
     * ui类型
     */
    @XStreamAlias("type")
    private UiType uiType;

    /**
     * ui类型
     */
    @XStreamAlias("dataType")
    private Type dataType;

    @XStreamAlias("itemXml")
    private List<ItemXml> itemXml;

    @XStreamAlias("itemFrom")
    private ItemFrom itemFrom = ItemFrom.xml;
    /**
     * 选择项单选还是多选
     */
    @XStreamAlias("status")
    private Status status = Status.single;

    @XStreamAlias("readonly")
    private Boolean readonly = false;

    @XStreamAlias("visible")
    private Boolean visible = true;

    @XStreamAlias("inputType")
    private InputType inputType;

    @XStreamAlias("VerifyXml")
    private VerifyXml verifyXml;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setLabel(String label) {
        if (view instanceof ComplexTextView) {
            ((ComplexTextView) view).setText(label);
        }
        label = StringUtil.removeEmpty(label);
        if (EasyUtil.canConcat(this)) {
            String codes = EasyUtil.label2Codes(this, label);
            setValue(codes);
        } else {
            setValue(convertRunTimeObj(label));
        }
    }

    public UiType getUiType() {
        return uiType;
    }

    public void setUiType(UiType uiType) {
        this.uiType = uiType;
    }

    public Type getDataType() {
        return dataType;
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public List<ItemXml> getItemXml() {
        return itemXml;
    }

    public void setItemXml(List<ItemXml> itemXml) {
        this.itemXml = itemXml;
    }

    public void addItemXml(ItemXml itemXml) {
        if (this.itemXml == null) this.itemXml = new ArrayList<>();
        this.itemXml.add(itemXml);
    }

    public void addItemXml(List<ItemXml> itemXml) {
        if (this.itemXml == null) this.itemXml = new ArrayList<>();
        this.itemXml.addAll(itemXml);
    }

    public ItemFrom getItemFrom() {
        return itemFrom == null ? ItemFrom.xml : itemFrom;
    }

    public void setItemFrom(ItemFrom itemFrom) {
        this.itemFrom = itemFrom;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getReadonly() {
        return readonly == null ? false : readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getVisible() {
        return visible == null ? true : visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public boolean isVerify() {
        return verifyXml != null;
    }

    public VerifyXml getVerifyXml() {
        return verifyXml;
    }

    public void setVerifyXml(VerifyXml verifyXml) {
        this.verifyXml = verifyXml;
    }

    @XStreamOmitField
    private View view;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }


    /**
     * 获取控件绑定的Label
     *
     * @return
     */
    public Object getViewLabel() {
        if (view == null) return null;
        String label = null;
        if (view instanceof ComplexTextView) {
            label = ((ComplexTextView) view).getText();
        }
        if (view instanceof ImageView) {
            label = ((EventImageView) view).getJson();
        }
        label = StringUtil.removeEmpty(label);
        setValue(convertRunTimeObj(label));
        return getValue();
    }

    /**
     * 获取控件绑定的code（需要映射）或 value
     *
     * @return
     */
    public Object getViewCode() {
        if (view == null) return null;
        String label = null;
        if (view instanceof ComplexTextView) {
            label = ((ComplexTextView) view).getText();
        }
        if (view instanceof EventImageView) {
            label = ((EventImageView) view).getJson();
        }
        label = StringUtil.removeEmpty(label);
        if (EasyUtil.canConcat(this)) {
            String codes = EasyUtil.label2Codes(this, label);
            setValue(codes);
        } else {
            setValue(convertRunTimeObj(label));
        }
        return getValue();
    }

    public boolean checkVerify() {
        if (view instanceof ComplexTextView) {
            return ((ComplexTextView) view).checkVerify();
        }
        return true;
    }

    public boolean verify() {
        if (verifyXml == null) return true;
        String regex = verifyXml.getRegex();
        getViewLabel();
        String text = StringUtil.removeNull(value);
        if (verifyXml.getCanNull() && StringUtil.isEmpty(text)) return true;
        boolean verify = RegexUtil.match(regex, text);
        if (!verify && view instanceof ComplexTextView) {
            ((ComplexTextView) view).setError(verifyXml.getMsg());
        }
        return verify;
    }

    public Object getDisPlay() {
        boolean canConcat = EasyUtil.canConcat(this);
        if (!canConcat) return value;
        return EasyUtil.code2Labels(this, value + "");
    }


    public Object convertRunTimeObj(String obj) {
        if (obj == null) return null;
        return ObjectUtil.str2SimpleObj(dataType, obj);
    }

    public UiType getUiTypeSmart() {
        if (uiType != null) return uiType;
        if (isDate()) return UiType.date;
        if (isDialogSel()) return UiType.dialog;
        if (isImg()) return UiType.img;
        if (isMultiSel()) return UiType.checkbox;
        if (isSingleSel()) return UiType.radio;
        return UiType.edit;
    }

    /**
     * 判断字段是不是数字类型
     *
     * @return
     */
    public boolean isNumber() {
        return NumberUtil.isNumber(dataType);
    }

    public boolean isDate() {
        if (getUiType() == UiType.date) {
            return true;
        }
        return false;
    }


    public boolean isImg() {
        if (getUiType() == UiType.img) return true;
        return false;
    }

    public boolean isDialogSel() {
        if (getUiType() == UiType.dialog) return true;
        List<ItemXml> codedValues = getItemXml();
        if (codedValues == null || codedValues.size() == 0) return false;
        return true;
    }

    public boolean isSingleSel() {
        if (getUiType() == UiType.radio) return true;
        List<ItemXml> codedValues = getItemXml();
        if (codedValues == null || codedValues.size() == 0) return false;
        return true;
    }

    public boolean isMultiSel() {
        if (getUiType() == UiType.checkbox) return true;
        List<ItemXml> codedValues = getItemXml();
        if (codedValues == null || codedValues.size() == 0) return false;
        return true;
    }
}
