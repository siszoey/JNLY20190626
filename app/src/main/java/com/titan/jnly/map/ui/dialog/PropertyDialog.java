package com.titan.jnly.map.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.lib.bandaid.arcruntime.layer.info.Field;
import com.lib.bandaid.arcruntime.layer.info.LayerInfo;
import com.lib.bandaid.utils.ObjectUtil;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.lib.bandaid.widget.layout.EntityLayoutView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/6/3.
 */

public class PropertyDialog extends BaseDialogFrg {

    public static PropertyDialog newInstance(String title, LayerInfo info, Map property) {
        PropertyDialog fragment = new PropertyDialog();
        fragment.setProperty(property);
        fragment.setInfo(info);
        fragment.setTitle(title);
        return fragment;
    }

    private String title;
    private LayerInfo info;
    private Map property;
    private EntityLayoutView entityLayoutView;

    public void setInfo(LayerInfo info) {
        this.info = info;
    }

    private void setProperty(Map property) {
        this.property = property;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, title, Gravity.CENTER);
        w = 0.8f;
        h = 0.8f;
        entityLayoutView = new EntityLayoutView<Map>(getContext());
        setContentView(entityLayoutView);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        if (info != null) {
            Map<String, Object> _property = new HashMap<>();
            List<Field> fields = info.getFields();
            Field field;
            boolean equal = false;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                for (Object name : property.keySet()) {
                    equal = ObjectUtil.baseTypeIsEqual(name, field.getName());
                    if (equal) {
                        _property.put(field.getAlias(), property.get(name));
                        break;
                    }
                }
            }
            entityLayoutView.setData(_property).resolutionData();
        }else {
            entityLayoutView.setData(property).resolutionData();
        }

    }
}
