package com.titan.jnly.map.ui.tools;

import android.view.View;

import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;
import com.titan.jnly.map.ui.frame.VectorBar;

public class ToolEdit extends BaseTool {

    public ToolEdit() {
        id = getClass().getSimpleName();
        name = "小班编辑";
        resId = R.mipmap.ic_map_edit_normal;
        checkedResId = R.mipmap.ic_map_edit_pressed;
    }

    @Override
    public boolean isCheckBtn() {
        return true;
    }

    @Override
    public void activate() {
        super.activate();
        arcMap.getWidgetContainer().getWidget(VectorBar.class).show();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        arcMap.getWidgetContainer().getWidget(VectorBar.class).hide();
    }
}
