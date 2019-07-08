package com.titan.jnly.map.ui.tools;

import android.view.View;

import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;

public class ToolClear extends BaseTool {

    public ToolClear() {
        id = getClass().getSimpleName();
        name = "清空";
        /*resId = R.drawable.ic_map_clear;
        checkedResId = R.drawable.ic_tool_pressed;*/

        resId = R.mipmap.ic_map_clear_normal;
        checkedResId = R.mipmap.ic_map_clear_pressed;
    }

    @Override
    public void viewClick(View view) {
        super.viewClick(view);
        arcMap.getGraphicContainer().clear();
    }
}
