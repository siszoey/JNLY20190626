package com.lib.bandaid.widget.treeview.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.bandaid.R;
import com.lib.bandaid.widget.treeview.base.BaseNodeViewBinder;
import com.lib.bandaid.widget.treeview.bean.TreeNode;


/**
 * Created by Administrator on 2017/7/4.
 */

public class Level0 extends BaseNodeViewBinder {

    private ImageView image_arrow;
    private TextView all_name;

    public Level0(View itemView) {
        super(itemView);
        image_arrow = itemView.findViewById(R.id.image_arrow);
        all_name = itemView.findViewById(R.id.all_name);
    }


    @Override
    public int getLayoutId() {
        return R.layout.widget_tree_view_level0_item;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        all_name.setText(treeNode.getLabel() + "");
    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            image_arrow.setImageResource(R.mipmap.widget_tree_view_icon_expended);
        } else {
            image_arrow.setImageResource(R.mipmap.widget_tree_view_icon_unexpend);
        }
    }
}
