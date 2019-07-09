package com.titan.jnly.vector.inter;

import android.content.Context;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.titan.jnly.vector.bean.RepealInfo;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public interface IMap {

    MapView getMapview();

    Context getContext();

    AppCompatActivity getActivity();

    ArrayList<RepealInfo> getRepairList();

}
