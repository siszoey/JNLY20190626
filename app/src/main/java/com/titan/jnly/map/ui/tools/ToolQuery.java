package com.titan.jnly.map.ui.tools;

import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;

public class ToolQuery extends BaseTool {

    public ToolQuery() {
        id = getClass().getSimpleName();
        name = "识别";
        resId = R.drawable.ic_map_query;
        checkedResId = R.drawable.ic_tool_pressed;
    }
}
