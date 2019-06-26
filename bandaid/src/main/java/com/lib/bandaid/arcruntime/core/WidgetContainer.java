package com.lib.bandaid.arcruntime.core;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.esri.arcgisruntime.layers.Layer;
import com.lib.bandaid.arcruntime.tools.core.ToolGroup;
import com.lib.bandaid.utils.CollectUtil;
import com.lib.bandaid.utils.ReflectUtil;
import com.lib.bandaid.utils.ViewUtil;
import com.lib.bandaid.R;
import com.lib.bandaid.widget.base.EGravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/5/10.
 */

public class WidgetContainer extends BaseContainer {

    //类Clazz + object...  参数列表
    private final static List<Map> _registerList = new ArrayList<>();

    public static void registerWidget(Class<? extends BaseMapWidget> clazz, EGravity eGravity) {
        Map widget = new HashMap();
        widget.put("clazz", clazz);
        widget.put("params", new Object[]{eGravity.getValue()});
        _registerList.add(widget);
    }

    public static void registerWidget(Class<? extends BaseMapWidget> clazz) {
        Map widget = new HashMap();
        widget.put("clazz", clazz);
        widget.put("params", new Object[]{});
        _registerList.add(widget);
    }


    private Map<String, BaseMapWidget> widgetMap = new HashMap<>();

    private ViewGroup rootView;

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.band_aid_core_widget_layout, arcMap);
        for (Map map : _registerList) {
            initWidget(map);
        }
        _registerList.clear();
    }

    @Override
    public void ready(List<Layer> layers) {
        super.ready(layers);
    }

    @Override
    public void destroy() {
        super.destroy();
    }


    private void initWidget(Map map) {
        if (map == null) return;
        Class clazz = (Class) map.get("clazz");
        Object[] params = (Object[]) map.get("params");
        params = CollectUtil.addArray(context, params);
        BaseMapWidget mapWidget = ReflectUtil.newInstance(clazz, params);
        if (mapWidget == null) return;
        mapWidget.create(arcMap);
        widgetMap.put(mapWidget.getClass().getName(), mapWidget);
        rootView.addView(mapWidget.getView());
    }

    public void addWidget(BaseMapWidget mapWidget) {
        if (mapWidget == null) return;
        mapWidget.create(arcMap);
        widgetMap.put(mapWidget.getClass().getName(), mapWidget);
        rootView.addView(mapWidget.getView());
    }

    public void removeWidget(BaseMapWidget mapWidget) {
        if (mapWidget == null) return;
        mapWidget.onDestroy();
        widgetMap.remove(mapWidget.getClass().getName());
        rootView.removeView(mapWidget.getView());
    }

    public BaseMapWidget getWidget(Class clazz) {
        String name = clazz.getName();
        return widgetMap.get(name);
    }

    protected void addToolGroup(ToolGroup toolGroup) {
        rootView.removeView(toolGroup.getGroupView());
        EGravity gravity = EGravity.getEnumByValue(toolGroup.getGravity());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (gravity == EGravity.TOP || gravity == EGravity.LEFT_TOP) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (gravity == EGravity.BOTTOM || gravity == EGravity.LEFT_BOTTOM) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (gravity == EGravity.RIGHT_TOP || gravity == EGravity.RIGHT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else if (gravity == EGravity.RIGHT_BOTTOM) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if (gravity == EGravity.CENTER) {
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (gravity == EGravity.LEFT_CENTER) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if (gravity == EGravity.TOP_CENTER) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (gravity == EGravity.BOTTOM_CENTER) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if (gravity == EGravity.RIGHT_CENTER) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        rootView.addView(toolGroup.getGroupView(), layoutParams);
    }


    public <T extends View> T $(int resId) {
        return ViewUtil.findViewById(rootView, resId);
    }
}
