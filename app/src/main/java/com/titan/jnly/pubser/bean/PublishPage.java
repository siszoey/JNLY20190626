package com.titan.jnly.pubser.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
public class PublishPage implements Serializable {

    private Integer total;

    private List<Publish> rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Publish> getRows() {
        return rows;
    }

    public void setRows(List<Publish> rows) {
        this.rows = rows;
    }
}
