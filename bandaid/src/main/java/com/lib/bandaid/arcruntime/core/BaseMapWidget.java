package com.lib.bandaid.arcruntime.core;

import android.content.Context;

import com.esri.arcgisruntime.layers.Layer;
import com.lib.bandaid.widget.base.BaseWidget;

import java.util.List;

/**
 * Created by zy on 2019/5/24.
 */

public abstract class BaseMapWidget extends BaseWidget implements IContainer {

    protected ArcMap arcMap;

    public BaseMapWidget(Context context) {
        super(context);
    }

    @Override
    public void create(ArcMap arcMap) {
        this.arcMap = arcMap;
    }

    @Override
    public void ready(List<Layer> layers) {

    }

    @Override
    public void destroy() {

    }
}
