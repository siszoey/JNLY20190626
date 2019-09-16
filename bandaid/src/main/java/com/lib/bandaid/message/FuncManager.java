package com.lib.bandaid.message;

import android.text.TextUtils;

import com.lib.bandaid.util.SoftHashMap;


public class FuncManager {

    private static volatile FuncManager instance;
    private SoftHashMap<String, Function> functionHashMap;

    private FuncManager() {
        functionHashMap = new SoftHashMap<>();
    }

    public static FuncManager getInstance() {
        if (instance == null) {
            synchronized (FuncManager.class) {
                if (instance == null) {
                    instance = new FuncManager();
                }
            }
        }
        return instance;
    }

    public void removeFunc(String... funcNames) {
        if (funcNames == null || funcNames.length == 0) return;
        for (String name : funcNames) {
            functionHashMap.remove(name);
        }
    }

    /**
     * 管理类添加  无参无返
     *
     * @param function
     * @return 链式调用返回本身
     */
    public FuncManager addFunc(FuncNoParamNoResult function) {
        functionHashMap.put(function.funcName, function);
        return this;
    }

    /**
     * 管理类调用  无参无返
     *
     * @param name
     */
    public void invokeFunc(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        if (functionHashMap != null) {
            Function fNPNR = functionHashMap.get(name);
            if (fNPNR instanceof FuncNoParamNoResult) {
                ((FuncNoParamNoResult) fNPNR).function();
            } else
                try {
                    throw new Exception("Has no this function" + name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }


    /**
     * 管理类添加  无参有返
     *
     * @param function
     * @return 链式调用返回本身
     */
    public FuncManager addFunc(FuncNoParamWithResult function) {
        functionHashMap.put(function.funcName, function);
        return this;
    }

    /**
     * 管理类调用 无参有返
     *
     * @param name     接口名字
     * @param clz      万能对象
     * @param <Result> 参数类型
     * @return 返回类型
     */
    public <Result> Result invokeFunc(String name, Class<Result> clz) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        if (functionHashMap != null) {
            Function function = functionHashMap.get(name);
            if (function instanceof FuncNoParamWithResult) {
                FuncNoParamWithResult fNPWR = (FuncNoParamWithResult) function;
                if (clz != null) {
                    return clz.cast(fNPWR.function());
                } else {
                    return (Result) fNPWR.function();
                }
            } else {
                try {
                    new Exception("Has no this function" + name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


    /**
     * 管理类 添加有参有返
     *
     * @param function 实例
     * @return 链式调用
     */
    public FuncManager addFunc(FuncWithParamWithResult function) {
        functionHashMap.put(function.funcName, function);
        return this;
    }

    /**
     * 管理类调用 有参有返
     *
     * @param name     接口名字
     * @param clz      万能对象
     * @param <Result> 参数类型
     * @return 返回类型
     */
    public <Result, Param> Result invokeFunc(String name, Class<Result> clz, Param data) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        if (functionHashMap != null) {
            Function function = functionHashMap.get(name);
            if (function instanceof FuncWithParamWithResult) {
                FuncWithParamWithResult fWPWR = (FuncWithParamWithResult) function;
                if (clz != null) {
                    return clz.cast(fWPWR.function(data));
                } else {
                    return (Result) fWPWR.function(data);
                }
            } else {
                try {
                    throw new Exception("Has no this function" + name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 管理类 添加有参无返
     *
     * @param function
     * @return
     */
    public FuncManager addFunc(FuncWithParamNoResult function) {
        functionHashMap.put(function.funcName, function);
        return this;
    }

    /**
     * 管理类调用 有参无返
     *
     * @param name    名字
     * @param data    参数
     * @param <Param> 泛型
     */
    public <Param> void invokeFunc(String name, Param data) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        if (functionHashMap != null) {
            Function function = functionHashMap.get(name);
            if (function instanceof FuncWithParamNoResult) {
                FuncWithParamNoResult fWPWR = (FuncWithParamNoResult) function;
                fWPWR.function(data);
            } else {
                try {
                    throw new Exception("Has no this function" + name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
