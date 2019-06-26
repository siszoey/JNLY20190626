package com.lib.bandaid.arcruntime.util;

import com.esri.arcgisruntime.data.Feature;
import com.google.gson.Gson;
import com.lib.bandaid.utils.GsonFactory;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.utils.ReflectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/5/14.
 */

public final class FeatureUtil {

    private FeatureUtil() {

    }

    public static <T> T getExtendFromFeature(Feature feature, Class clazz) {
        Gson gson = GsonFactory.getFactory().getComGson();
        String json = (String) feature.getAttributes().get("EXTEND");
        return (T) gson.fromJson(json, clazz);
    }


    public static <T> List<FeatureTaker<T>> mergeByFeature(List<Feature> features, List<T> list, Map<String, String> attr) {
        if (features == null && list == null) return null;
        List<FeatureTaker<T>> featureTakers = new ArrayList<>();
        FeatureTaker<T> taker;
        boolean isEqual = false;
        for (Feature feature : features) {
            for (T info : list) {
                isEqual = compareFeature(feature, info, attr);
                if (isEqual) {
                    taker = FeatureTaker.create(feature, info);
                    featureTakers.add(taker);
                    break;
                }
            }
        }
        return featureTakers;
    }

    public static <T> List<FeatureTaker<T>> mergeByExtend(List<T> list, List<Feature> features, Map<String, String> attr) {
        if (features == null || list == null) return null;
        List<FeatureTaker<T>> featureTakers = new ArrayList<>();
        FeatureTaker<T> taker;
        boolean isEqual = false;
        for (T info : list) {
            for (Feature feature : features) {
                isEqual = compareFeature(feature, info, attr);
                if (isEqual) {
                    taker = FeatureTaker.create(feature, info);
                    featureTakers.add(taker);
                    break;
                }
            }
        }
        return featureTakers;
    }

    public static<T> List<FeatureTaker<T>> convertFeatureTaker(List<Feature> features) {
        if (features == null) return null;
        List<FeatureTaker<T>> featureTakers = new ArrayList<>();
        FeatureTaker taker;
        for (Feature feature : features) {
            taker = FeatureTaker.create(feature, null);
            featureTakers.add(taker);
        }
        return featureTakers;
    }


    public static Object getFeatureAttr(Feature feature, String key) {
        if (feature == null) return null;
        return feature.getAttributes().get(key);
    }


    /**
     * 比较feature和entity 的属性是否相同
     *
     * @param feature
     * @param entity
     * @param attr
     * @return
     */
    public static boolean compareFeature(Feature feature, Object entity, Map<String, String> attr) {
        Object o1, o2;
        String key2;
        Boolean equal = true;
        for (String key1 : attr.keySet()) {
            key2 = attr.get(key1);
            o1 = getFeatureAttr(feature, key1);
            o2 = ReflectUtil.getFieldValue(entity, key2);
            if (!ObjectUtil.baseTypeIsEqual(o1, o2)) {
                equal = false;
                break;
            }
        }
        return equal;
    }


}
