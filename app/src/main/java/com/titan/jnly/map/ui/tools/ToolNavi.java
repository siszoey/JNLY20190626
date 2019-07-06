package com.titan.jnly.map.ui.tools;

import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;

public class ToolNavi extends BaseTool {

    public ToolNavi() {
        id = getClass().getSimpleName();
        name = "导航";
        resId = R.drawable.ic_map_navi;
        checkedResId = R.drawable.ic_tool_pressed;
    }
}
