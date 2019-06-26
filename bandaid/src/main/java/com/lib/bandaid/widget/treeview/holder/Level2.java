package com.lib.bandaid.widget.treeview.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.bandaid.R;
import com.lib.bandaid.widget.treeview.base.CheckableNodeViewBinder;
import com.lib.bandaid.widget.treeview.bean.TreeNode;


/**
 * Created by Administrator on 2017/7/4.
 */

public class Level2 extends CheckableNodeViewBinder {
    private TextView text_all_Select;
    private CheckBox check_select_all;
    private ImageView image_arrow;

    public Level2(View itemView) {
        super(itemView);
        image_arrow =  itemView.findViewById(R.id.image_arrow);
        text_all_Select =  itemView.findViewById(R.id.text_all_Select);
        check_select_all = itemView.findViewById(R.id.check_select_all);
    }

    @Override
    public int getArrayIconViewId() {
        return R.id.image_arrow;
    }

    @Override
    public int getCheckableViewId() {
        return R.id.check_select_all;
    }


    @Override
    public int getLayoutId() {
        return R.layout.widget_tree_view_level2_item;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        /** 设置TextView数据，作为第一级类目 */
        text_all_Select.setText(treeNode.getLabel() + "");
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
