package com.lib.bandaid.widget.snackbar;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by zy on 2018/9/10.
 */

class RecyclerUtil {
    static void setScrollListener(final Snackbar snackbar, View view) {
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                snackbar.dismiss();
            }
        });
    }
}
