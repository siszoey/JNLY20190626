package com.lib.bandaid.widget.treeview.holder;

import android.view.View;

import com.lib.bandaid.widget.treeview.base.BaseNodeViewBinder;
import com.lib.bandaid.widget.treeview.base.BaseNodeViewFactory;


/**
 * Created by Administrator on 2017/7/4.
 */

public class ItemFactory extends BaseNodeViewFactory {
    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {
        switch (level){
            case 0:
                return new Level0(view);
            case 1:
                return new Level1(view);
            case 2:
                return new Level2(view);
            default:
                return new Level3(view);
        }
    }
}
