package com.titan.jnly.map.ui.tools;

import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;

public class ToolTrack extends BaseTool {

    public ToolTrack() {
        id = getClass().getSimpleName();
        name = "轨迹";
        resId = R.drawable.ic_map_track;
        checkedResId = R.drawable.ic_tool_pressed;
    }
}
