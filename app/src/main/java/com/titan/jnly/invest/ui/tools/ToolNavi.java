package com.titan.jnly.invest.ui.tools;

import android.view.View;

import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.lib.bandaid.arcruntime.tools.core.BaseTool;
import com.titan.jnly.R;

public class ToolNavi extends BaseTool{

    public ToolNavi() {
        id = getClass().getSimpleName();
        name = "导航";
        /*resId = R.drawable.ic_map_navi;
        checkedResId = R.drawable.ic_tool_pressed;*/
        resId = R.mipmap.ic_map_navi_normal;
        checkedResId = R.mipmap.ic_map_navi_pressed;
    }

    @Override
    public void viewClick(View view) {
        super.viewClick(view);
        AmapNaviPage.getInstance().showRouteActivity(context, new AmapNaviParams(null, null, null, AmapNaviType.DRIVER), null);
    }
}
