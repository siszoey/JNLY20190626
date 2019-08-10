package com.lib.bandaid.data.remote.entity;

import java.io.Serializable;

public class TTResult<T> implements Serializable {

    private int Code = 1000;

    private Boolean Result;

    private String Message;

    private T Content;

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
