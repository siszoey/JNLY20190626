package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.layers.Layer;

import java.util.List;

/**
 * Created by zy on 2019/5/9.
 */

public interface IContainer {
    public void create(ArcMap arcMap);

    public void ready(List<Layer> layers);

    public void destroy();
}
