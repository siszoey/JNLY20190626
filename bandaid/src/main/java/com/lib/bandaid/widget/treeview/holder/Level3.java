package com.lib.bandaid.widget.treeview.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lib.bandaid.R;
import com.lib.bandaid.widget.treeview.base.CheckableNodeViewBinder;
import com.lib.bandaid.widget.treeview.bean.TreeNode;


/**
 * Created by Administrator on 2017/7/4.
 */

public class Level3 extends CheckableNodeViewBinder {
    private TextView item_name;
    private CheckBox item_choice;

    public Level3(View itemView) {
        super(itemView);
        item_name =  itemView.findViewById(R.id.item_name);
        item_choice = itemView.findViewById(R.id.item_choice);
    }

    @Override
    public int getCheckableViewId() {
        return R.id.item_choice;
    }

    @Override
    public int getLayoutId() {
        return R.layout.widget_tree_view_level3_item;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        item_name.setText(treeNode.getLabel() + "");
    }
}
