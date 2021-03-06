package com.lib.bandaid.widget.easyui.xml;

import android.view.View;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by zy on 2019/7/21.
 */
@XStreamAlias("ItemXml")
public class ItemXml implements Serializable {

    @XStreamAlias("code")
    private Object code;
    @XStreamAlias("value")
    private String value;
    @XStreamAlias("checked")
    private Boolean checked = false;

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
