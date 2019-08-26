package com.titan.jnly.invest.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.camera.lib.util.MeasureScreen;
import com.esri.arcgisruntime.data.Feature;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.adapter.recycle.decoration.GroupItem;
import com.lib.bandaid.adapter.recycle.decoration.SectionDecoration;
import com.lib.bandaid.arcruntime.layer.project.LayerNode;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.titan.jnly.R;
import com.titan.jnly.invest.apt.FeatureAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FeatureDialog extends BaseDialogFrg implements BaseRecycleAdapter.IViewClickListener<GroupItem<Feature>>, SectionDecoration.PowerGroupListener {

    private FeatureAdapter featureAdapter;
    private RecyclerView rvFeatures;
    private List<GroupItem<Feature>> features;
    private SectionDecoration decoration;
    private ICallBack iCallBack;
    private String title = "";

    public static FeatureDialog newInstance() {
        FeatureDialog fragment = new FeatureDialog();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, title, Gravity.CENTER);
        setContentView(R.layout.map_ui_dialog_feature_sel);
    }

    public FeatureDialog initData(String title, Map<LayerNode, List<Feature>> data, ICallBack iCallBack) {
        this.title = title;
        this.iCallBack = iCallBack;
        if (data != null) {
            List<Feature> fea;
            features = new ArrayList<>();
            List<GroupItem<Feature>> items;
            for (LayerNode node : data.keySet()) {
                fea = data.get(node);
                items = GroupItem.convert(node.getName(), node, fea);
                features.addAll(items);
            }
        }
        return this;
    }

    @Override
    protected void initialize() {
        rvFeatures = $(R.id.rvFeatures);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        decoration = SectionDecoration.Builder.init(this).setGroupHeight(MeasureScreen.dip2px(context, 40)).build();
        rvFeatures.addItemDecoration(decoration);
        featureAdapter = new FeatureAdapter(rvFeatures);
        featureAdapter.setIViewClickListener(this);
        featureAdapter.replaceAll(features);
    }

    @Override
    public String getGroupName(int position) {
        return featureAdapter.getItem(position).getName();
    }

    @Override
    public View getGroupView(int position) {
        return featureAdapter.initGroupView(position);
    }

    @Override
    public void onClick(View view, GroupItem<Feature> data, int position) {
        if (iCallBack != null) iCallBack.callBack(data);
        dismiss();
    }

    public interface ICallBack {
        public void callBack(GroupItem<Feature> data);
    }
}
