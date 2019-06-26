package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;

/**
 * Created by zy on 2019/5/12.
 */

public class GraphicContainer extends BaseContainer {

    GraphicsOverlay graphicsOverlay;

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        this.graphicsOverlay = new GraphicsOverlay();
    }
}
