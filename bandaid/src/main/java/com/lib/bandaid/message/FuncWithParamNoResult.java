package com.lib.bandaid.message;

public abstract class FuncWithParamNoResult<Param> extends Function {
    /**
     * 构造方法
     *
     * @param funcName 接口的名字，必须实现
     */
    public FuncWithParamNoResult(String funcName) {
        super(funcName);
    }

    /**
     * 有参数，无返回值
     */
    public abstract void function(Param data);
}
