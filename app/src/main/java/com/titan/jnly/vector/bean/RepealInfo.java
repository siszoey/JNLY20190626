package com.titan.jnly.vector.bean;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.layers.FeatureLayer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 小班修改撤销信息
 */
public class RepealInfo implements Serializable {

    private static final long serialVersionUID = 7492244532276134304L;
    //小班id
    private String objectid;
    //修改后的小班图形
    private Feature ugeometry;
    //修改的小班
    private Feature bfeature;
    //修改类型 add添加 del删除 update修改 hebing合并
    private String type;
    //小班所在图层
    private FeatureLayer layer;
    //图层所在数据表
    private FeatureTable table;
    //小班属性集合
    private Map<String, Object> att;

    private List<Feature> features;


    public Feature getUgeometry() {
        return ugeometry;
    }

    public void setUgeometry(Feature ugeometry) {
        this.ugeometry = ugeometry;
    }

    public Feature getBfeature() {
        return bfeature;
    }

    public void setBfeature(Feature bfeature) {
        this.bfeature = bfeature;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FeatureLayer getLayer() {
        return layer;
    }

    public void setLayer(FeatureLayer layer) {
        this.layer = layer;
    }

    public FeatureTable getTable() {
        return table;
    }

    public void setTable(FeatureTable table) {
        this.table = table;
    }

    public Map<String, Object> getAtt() {
        return att;
    }

    public void setAtt(Map<String, Object> att) {
        this.att = att;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
