package com.titan.jnly.main.ui.frg.watch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.titan.jnly.R;
import com.titan.jnly.common.fragment.BaseMainFragment;

public class WatchFragment extends BaseMainFragment {

    public static WatchFragment newInstance() {
        Bundle args = new Bundle();
        WatchFragment fragment = new WatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_ui_frg_watch_layout, container, false);
        return view;
    }
}