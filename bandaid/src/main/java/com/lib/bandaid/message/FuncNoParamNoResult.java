package com.lib.bandaid.message;

public abstract class FuncNoParamNoResult extends Function {
    /**
     * 构造方法
     *
     * @param functionName 接口的名字，必须实现
     */
    public FuncNoParamNoResult(String functionName) {
        super(functionName);
    }

    /**
     * 无参数
     */
    public abstract void function();
}
