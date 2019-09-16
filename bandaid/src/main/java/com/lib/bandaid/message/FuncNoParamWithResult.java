package com.lib.bandaid.message;

public abstract class FuncNoParamWithResult<Result> extends Function {
    /**
     * 构造方法
     *
     * @param funcName 接口的名字，必须实现
     */
    public FuncNoParamWithResult(String funcName) {
        super(funcName);
    }

    /**
     * 无参数，带有返回值
     * Result  泛型
     */
    public abstract Result function();
}
