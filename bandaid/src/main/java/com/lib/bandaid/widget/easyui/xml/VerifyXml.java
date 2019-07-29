package com.lib.bandaid.widget.easyui.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("VerifyXml")
public class VerifyXml implements Serializable {
    /**
     * 正则表达式
     */
    @XStreamAlias("regex")
    private String regex;
    /**
     * 提示
     */
    @XStreamAlias("msg")
    private String msg;


    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
