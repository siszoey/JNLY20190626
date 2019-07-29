package com.lib.bandaid.widget.easyui.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/7/21.
 */
@XStreamAlias("EasyUiXml")
public class EasyUiXml implements Serializable {

    public EasyUiXml() {

    }

    public static EasyUiXml create() {
        return new EasyUiXml();
    }

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("alias")
    private String alias;

    @XStreamAlias("uiXml")
    private List<UiXml> uiXml;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<UiXml> getUiXml() {
        return uiXml;
    }

    public UiXml getUiXml(String name) {
        if (uiXml == null) return null;
        for (UiXml ui : uiXml) {
            if (ui == null) continue;
            if (ui.getCode().equals(name)) return ui;
        }
        return null;
    }

    public void setUiXml(List<UiXml> uiXml) {
        this.uiXml = uiXml;
    }

    public void addUiXml(UiXml uiXml) {
        if (this.uiXml == null) this.uiXml = new ArrayList<>();
        this.uiXml.add(uiXml);
    }

    public void release() {
        if (uiXml == null) return;
        for (UiXml ui : uiXml) {
            ui.setView(null);
            ui.setValue(null);
        }
    }

    public Map<String, Object> getFormMap() {
        if (uiXml == null) return null;
        Map<String, Object> res = new HashMap<>();
        Object val;
        for (UiXml ui : uiXml) {
            val = ui.getViewCode();
            if (val == null) continue;
            res.put(ui.getCode(), val);
        }
        return res;
    }

    public boolean verifyForm() {
        if (uiXml == null) return false;
        boolean verify = true;
        for (UiXml ui : uiXml) {
            if (!ui.verify()) verify = false;
        }
        return verify;
    }
}
