package com.titan.jnly.map.ui.tools;

import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;

public class ToolClear extends BaseTool {

    public ToolClear() {
        id = getClass().getSimpleName();
        name = "清空";
        resId = R.drawable.ic_map_clear;
        checkedResId = R.drawable.ic_tool_pressed;
    }
}
