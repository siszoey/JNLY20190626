package com.lib.bandaid.arcruntime.tools;

import android.view.View;

import com.lib.bandaid.R;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;

public class ZoomLocal extends BaseTool {

    public ZoomLocal() {
        id = getClass().getSimpleName();
        name = "当前位置";
        resId = R.mipmap.tool_map_zoom_loc_normal;
        checkedResId = R.mipmap.tool_map_zoom_loc_pressed;
    }

    @Override
    public void viewClick(View view) {
        arcMap.getMapControl().initDefaultLocation().useDefaultLocation(100);
    }
}

