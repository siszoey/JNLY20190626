package com.titan.jnly.vector.bean;

import com.lib.bandaid.data.local.sqlite.core.annotation.Column;
import com.lib.bandaid.data.local.sqlite.core.annotation.Table;

import java.io.Serializable;

/**
 * 树龄计算模型
 */
@Table(name = "TB_TreeMode")
public class TreeMode implements Serializable {

    @Column(name = "f_id", isPKey = true, autoincrement = true)
    private Integer id;
    /**
     * 树种名称
     */
    @Column(name = "f_name", type = "varchar(20)")
    private String name;

    @Column(name = "f_diam_hill")
    private Double diamHill;

    @Column(name = "f_diam_plain")
    private Double diamPlain;

    @Column(name = "f_year")
    private Integer year;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiamHill() {
        return diamHill;
    }

    public void setDiamHill(Double diamHill) {
        this.diamHill = diamHill;
    }

    public Double getDiamPlain() {
        return diamPlain;
    }

    public void setDiamPlain(Double diamPlain) {
        this.diamPlain = diamPlain;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "TreeMode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", diamHill=" + diamHill +
                ", diamPlain=" + diamPlain +
                ", year=" + year +
                '}';
    }
}
