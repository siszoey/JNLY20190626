package com.titan.jnly.map.ui.frame;

import android.content.Context;

import com.lib.bandaid.arcruntime.core.BaseMapWidget;
import com.lib.bandaid.widget.base.EGravity;
import com.titan.jnly.R;

public class VectorBar extends BaseMapWidget {

    public VectorBar(Context context) {
        super(context);
        w = 0.8f;
        h = 0.2f;
        layoutGravity = EGravity.BOTTOM_CENTER.getValue();
        setContentView(R.layout.include_feature_tools);
    }


    @Override
    public void initialize() {

    }

    @Override
    public void registerEvent() {

    }

    @Override
    public void initClass() {

    }
}
