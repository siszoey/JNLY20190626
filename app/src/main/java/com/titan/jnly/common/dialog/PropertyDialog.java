package com.titan.jnly.common.dialog;

import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;

import com.esri.arcgisruntime.data.Field;
import com.lib.bandaid.widget.easyui.ui.MapLayoutView;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/6/3.
 */

public class PropertyDialog extends BaseDialogFrg {

    public static PropertyDialog newInstance(String title, List<Field> fields, Map property) {
        PropertyDialog fragment = new PropertyDialog();
        fragment.setTitle(title);
        fragment.setInfo(fields);
        fragment.setProperty(property);
        return fragment;
    }

    private String title;
    private Map property;
    private List<Field> fields;
    private MapLayoutView entityLayoutView;

    public void setTitle(String title) {
        this.title = title;
    }

    private void setInfo(List<Field> fields) {
        this.fields = fields;
    }

    private void setProperty(Map property) {
        this.property = property;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, title, Gravity.CENTER);
        w = 0.8f;
        h = 0.8f;
        entityLayoutView = new MapLayoutView(getContext());
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
        if (fields != null) {
            Map<Field, Object> _property = new HashMap<>();
            Field field;
            boolean equal = false;
            for (int i = 0, len = fields.size(); i < len; i++) {
                field = fields.get(i);
                for (Object name : property.keySet()) {
                    equal = ObjectUtil.baseTypeIsEqual(name, field.getName());
                    if (equal) {
                        _property.put(field, property.get(name));
                        break;
                    }
                }
            }
            entityLayoutView.setData(_property).resolutionData();
        } else {
            entityLayoutView.setData(property).resolutionData();
        }
    }
}
