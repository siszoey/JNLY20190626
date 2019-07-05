package com.titan.jnly.main.ui.frg.manage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.lib.bandaid.arcruntime.core.ArcMap;
import com.lib.bandaid.utils.ViewUtil;
import com.titan.jnly.R;
import com.titan.jnly.common.fragment.BaseMainFragment;

public class ManagerFragment extends BaseMainFragment implements ArcMap.IMapReady {

    public static ManagerFragment newInstance() {
        Bundle args = new Bundle();
        ManagerFragment fragment = new ManagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    View view;
    ArcMap arcMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_ui_frg_manage_layout, container, false);
            //arcMap = ViewUtil.findViewById(view, R.id.arcMap);
            //arcMap.mapLoad(this);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
        return view;
    }

    @Override
    public void onMapReady() {

    }
}
