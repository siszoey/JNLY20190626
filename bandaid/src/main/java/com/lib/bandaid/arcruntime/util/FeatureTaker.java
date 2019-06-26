package com.lib.bandaid.arcruntime.util;

import com.esri.arcgisruntime.data.Feature;

import java.io.Serializable;

/**
 * Created by zy on 2019/5/14.
 */

public class FeatureTaker<T> implements Serializable {

    private Feature feature;

    private T data;

    private FeatureTaker(Feature feature, T data) {
        this.feature = feature;
        this.data = data;
    }

    public static <T> FeatureTaker create(Feature feature, T data) {
        return new FeatureTaker(feature, data);
    }


    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
