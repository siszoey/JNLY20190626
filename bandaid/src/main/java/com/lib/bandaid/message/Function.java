package com.lib.bandaid.message;

public abstract class Function {

    public String funcName;


    /**
     * 构造方法
     *
     * @param funcName 接口的名字，必须实现
     */
    public Function(String funcName) {
        this.funcName = funcName;
    }
}
