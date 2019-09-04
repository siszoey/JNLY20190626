package com.titan.jnly.common.tools;

import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;

public class ToolLocal extends BaseTool {

    public ToolLocal() {
        id = getClass().getSimpleName();
        name = "当前位置";
        resId = R.drawable.ic_map_local;
        checkedResId = R.drawable.ic_tool_pressed;
    }
}
