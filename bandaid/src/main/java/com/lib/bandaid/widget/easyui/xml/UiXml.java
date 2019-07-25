package com.lib.bandaid.widget.easyui.xml;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.bandaid.data.local.sqlite.utils.ReflectUtil;
import com.lib.bandaid.utils.DateUtil;
import com.lib.bandaid.utils.NumberUtil;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.StringUtil;
import com.lib.bandaid.widget.easyui.enums.Status;
import com.lib.bandaid.widget.easyui.enums.UiType;
import com.lib.bandaid.widget.easyui.ui.EventImageView;
import com.lib.bandaid.widget.easyui.utils.EasyUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
    /**
     * ui名称
     */
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
    /**
     * 选择项单选还是多选
     */
    @XStreamAlias("status")
    private Status status = Status.single;

    @XStreamAlias("readOnly")
    private Boolean readOnly = false;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
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
     * 获取控件绑定的值
     *
     * @return
     */
    public Object getViewLabel() {
        if (view == null) return null;
        String label = null;
        if (view instanceof TextView) {
            label = ((TextView) view).getText().toString();
        }
        if (view instanceof ImageView) {
            label = ((EventImageView) view).getUuid();
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
        if (view instanceof TextView) {
            label = ((TextView) view).getText().toString();
        }
        if (view instanceof ImageView) {
            label = ((EventImageView) view).getUuid();
        }
        label = StringUtil.removeEmpty(label);
        if (EasyUtil.canConcat(this)) {
            String codes = EasyUtil.label2Codes(this, label);
            return codes;
        } else {
            setValue(convertRunTimeObj(label));
            return getValue();
        }
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
