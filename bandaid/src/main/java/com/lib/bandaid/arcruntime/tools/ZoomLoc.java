package com.lib.bandaid.arcruntime.tools;

import android.view.MotionEvent;
import android.view.View;

import com.lib.bandaid.R;
import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.lib.bandaid.arcruntime.tools.core.ToolView;

public class ZoomLoc extends BaseTool implements ToolView.ILongClick {

    public ZoomLoc() {
        id = getClass().getSimpleName();
        name = "当前位置";
        resId = R.mipmap.tool_map_zoom_loc_normal;
        checkedResId = R.mipmap.tool_map_zoom_loc_pressed;
    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        view.setILongClick(this);
    }

    @Override
    public void viewClick(View view) {
        arcMap.getMapControl().initDefaultLocation().useDefaultLocation(100);
    }

    @Override
    public void viewLongClick(View view) {

    }
}

