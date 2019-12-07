package com.lib.bandaid.data.remote.entity;

import java.io.Serializable;

public class TTResult<T> implements Serializable {

    public static TTResult Ok(Object data) {
        return new TTResult(200, "请求成功！", data, true);
    }

    public static TTResult Ok(String msg, Object data) {
        return new TTResult(200, msg, data, true);
    }

    public static TTResult Fail(String msg) {
        return new TTResult(0, msg, null, false);
    }

    private int Code = 1000;

    private Boolean Result;

    private String Message;

    private T Content;

    public TTResult() {

    }

    public TTResult(int code, String msg, T data, Boolean Result) {
        this.Code = code;
        this.Message = msg;
        this.Content = data;
        this.Result = Result;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public Boolean getResult() {
        return Result;
    }

    public void setResult(Boolean result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getContent() {
        return Content;
    }

    public void setContent(T content) {
        Content = content;
    }

    //----------------------------------------------------------------------------------------------
    public static <M> M convert(Object data) {
        try {
            return (M) data;
        } catch (Exception e) {
            return null;
        }
    }
}
