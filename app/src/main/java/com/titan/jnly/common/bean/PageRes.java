package com.titan.jnly.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * C# 服务端分页对象
 */
public class PageRes<T> implements Serializable {

    Integer total;

    List<T> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
