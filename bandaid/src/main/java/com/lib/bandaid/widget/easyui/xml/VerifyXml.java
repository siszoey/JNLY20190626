package com.lib.bandaid.widget.easyui.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 数据验证
 */
@XStreamAlias("VerifyXml")
public class VerifyXml implements Serializable {
    /**
     * 正则表达式
     */
    @XStreamAlias("regex")
    private String regex;
    /**
     * 是否允许为null
     */
    @XStreamAlias("canNull")
    private Boolean canNull;
    /**
     * 提示
     */
    @XStreamAlias("msg")
    private String msg;


    public String getRegex() {
        //return regex;
        return regex.replaceAll("\n", "").trim();
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Boolean getCanNull() {
        return canNull == null ? true : canNull;
    }

    public void setCanNull(Boolean canNull) {
        this.canNull = canNull;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
