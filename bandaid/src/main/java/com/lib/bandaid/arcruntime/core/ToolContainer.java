package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.layers.Layer;
import com.lib.bandaid.arcruntime.event.IArcMapEvent;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.lib.bandaid.arcruntime.tools.core.ToolGroup;
import com.lib.bandaid.widget.base.EGravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/5/28.
 */

public class ToolContainer extends BaseContainer {

    //类groupId + gravity + List<Class<? extends BaseTool>>  参数列表
    private final static List<Map> _registerList = new ArrayList<>();

    private final List<ToolGroup> _toolGroups = new ArrayList<>();

    public static void registerTool(Class<? extends BaseTool> tool, EGravity gravity) {
        registerTool(tool.getSimpleName(), gravity, true, tool);
    }

    public static void registerTool(String groupId, EGravity gravity, Class<? extends BaseTool>... tools) {
        registerTool(groupId, gravity, true, tools);
    }

    public static void registerTool(String groupId, EGravity gravity, boolean visible, Class<? extends BaseTool>... tools) {
        if (tools == null) return;
        List<Class<? extends BaseTool>> clazz = new ArrayList<>();
        for (int i = 0; i < tools.length; i++) {
            clazz.add(tools[i]);
        }
        Map map = new HashMap();
        map.put("groupId", groupId);
        map.put("gravity", gravity.getValue());
        map.put("visible", visible);
        map.put("classes", clazz);
        _registerList.add(map);
    }


    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);

        Map map;
        int gravity;
        String groupId;
        boolean visible;
        ToolGroup toolGroup;
        List<Class<? extends BaseTool>> classes;
        for (int i = 0; i < _registerList.size(); i++) {
            map = _registerList.get(i);
            groupId = (String) map.get("groupId");
            gravity = (int) map.get("gravity");
            visible = map.get("visible") == null ? true : (Boolean) map.get("visible");
            classes = (List<Class<? extends BaseTool>>) map.get("classes");
            toolGroup = new ToolGroup(groupId, visible, gravity);
            toolGroup.addItems(classes);
            toolGroup.create(arcMap);
            arcMap.getWidgetContainer().addToolGroup(toolGroup);
            _toolGroups.add(toolGroup);
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

    @Override
    public void activate(Object o) {
        boolean isRegisterMapEvent = false;
        if (o instanceof BaseTool) {
            ToolGroup toolGroup;
            BaseTool baseTool = (BaseTool) o;
            BaseTool _baseTool;
            isRegisterMapEvent = baseTool.isRegisterMapEvent();
            for (int i = 0; i < _toolGroups.size(); i++) {
                toolGroup = _toolGroups.get(i);
                for (int j = 0; j < toolGroup.getCount(); j++) {
                    _baseTool = toolGroup.getItem(j);
                    if (baseTool != null) {
                        if (_baseTool.getId().equals(baseTool.getId())) continue;
                    }
                    if (isRegisterMapEvent == _baseTool.isRegisterMapEvent()) {
                        _baseTool.deactivate();
                    }
                }
            }
        }
        if (isRegisterMapEvent) {
            if (o instanceof IArcMapEvent) arcMap.setEvent((IArcMapEvent) o);
        }
    }

    @Override
    public void deactivate(Object o) {
        if (o instanceof BaseTool) {
            if (((BaseTool) o).isRegisterMapEvent()) arcMap.setEvent(null);
        }
    }
}
