package com.lib.bandaid.adapter.spinner.event;

import android.view.View;

/**
 * Created by zy on 2019/4/23.
 */

public interface ItemClickListener<T> {

    public void itemSelected(View view, T data, int position);

}
