package com.titan.jnly.main.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.widget.squareview.GridItem;
import com.lib.bandaid.widget.squareview.LineGridView;
import com.lib.bandaid.widget.squareview.SquareAdapter;
import com.titan.jnly.R;
import com.titan.jnly.invest.ui.aty.InvestActivity;
import com.titan.jnly.patrol.ui.aty.PatrolActivity;

import java.util.ArrayList;
import java.util.List;

public class MainFaceAty extends BaseMvpCompatAty implements AdapterView.OnItemClickListener {

    private LineGridView gridView;
    private List<GridItem> items;
    private SquareAdapter squareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, "济南古树", Gravity.CENTER);
        setContentView(R.layout.main_ui_aty_face_layout);
    }

    @Override
    protected void initialize() {
        gridView = $(R.id.iconParent);
    }

    @Override
    protected void registerEvent() {
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void initClass() {
        items = new ArrayList<>();
        items.add(GridItem.create(R.mipmap.ic_launcher, "古树名木调查"));
        items.add(GridItem.create(R.mipmap.ic_launcher, "古树名木巡查"));
        items.add(GridItem.create(R.mipmap.ic_launcher, "古树名木管护"));
        squareAdapter = new SquareAdapter(this, items);
        gridView.setAdapter(squareAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            startActivity(new Intent(_context, InvestActivity.class));
        }
        if (position == 1) {
            startActivity(new Intent(_context, PatrolActivity.class));
        }
        if (position == 2) {
            showToast("功能尚未完成");
        }
    }
}
