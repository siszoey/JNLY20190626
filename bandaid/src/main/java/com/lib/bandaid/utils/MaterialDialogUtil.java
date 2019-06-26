package com.lib.bandaid.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

/**
 * Created by sp on 2018/11/15.
 * 提示弹窗
 */
public class MaterialDialogUtil {

    /**
     * 进度弹窗
     */
    public static MaterialDialog showLoadProgress(Context context, String msg, boolean isCancel) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.content(msg);
        builder.progress(true, 0);
        builder.cancelable(isCancel);
        builder.canceledOnTouchOutside(isCancel);
        return builder.build();
    }

    /**
     * 确认弹窗
     */
    public static MaterialDialog showSureDialog(Context context, String msg, MaterialDialog.SingleButtonCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title("提示");
        builder.positiveText("确定");
        builder.negativeText("取消");
        builder.content(msg);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.onPositive(callback);
        return builder.build();
    }

    /**
     * 单选弹窗
     */
    public static MaterialDialog showSingleSelectionDialog(Context context, String title, List list, MaterialDialog.ListCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(title);
        builder.negativeText("取消");
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.items(list);
        builder.itemsCallback(callback);
        return builder.build();
    }

    /**
     * 列表弹窗
     */
    public static MaterialDialog showItemDetailsDialog(Context context, String title, RecyclerView.Adapter adapter, LinearLayoutManager layoutManager) {
        return new MaterialDialog.Builder(context)
                .adapter(adapter, layoutManager)
                .title(title)
                .positiveText("确定")
                .build();
    }
}
