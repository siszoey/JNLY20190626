package com.lib.bandaid.widget.treeview.adapter.i;

import com.lib.bandaid.widget.treeview.bean.TreeNode;

/**
 * Created by zy on 2018/1/1.
 */

public interface ITreeViewNodeListening {
    public void nodeCheckListening(boolean checked, TreeNode treeNode);

    public void nodeClickListening(TreeNode treeNode);

    public void nodeLongClickListening(TreeNode treeNode);
}
