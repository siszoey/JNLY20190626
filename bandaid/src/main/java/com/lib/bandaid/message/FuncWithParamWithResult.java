package com.lib.bandaid.message;

public abstract class FuncWithParamWithResult<Param, Result> extends Function {
    /**
     * 构造方法
     *
     * @param funcName 接口的名字，必须实现
     */
    public FuncWithParamWithResult(String funcName) {
        super(funcName);
    }

    /**
     * 有参数，带有返回值
     */
    public abstract Result function(Param data);
}
