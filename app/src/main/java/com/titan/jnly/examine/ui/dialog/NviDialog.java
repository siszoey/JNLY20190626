package com.titan.jnly.examine.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.lib.bandaid.widget.edittext.ClearEditText;
import com.titan.jnly.R;

public class NviDialog extends BaseDialogFrg implements View.OnClickListener {

    private ClearEditText editText;
    private Button btnSearch;
    private RecyclerView rvList;

    public static NviDialog newInstance(ICallBack iCallBack) {
        NviDialog fragment = new NviDialog();
        Bundle bundle = new Bundle();
        //bundle.putSerializable("data", iCallBack);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam_ui_dialog_nvi_layout);
    }

    @Override
    protected void initialize() {
        editText = $(R.id.editText);
        btnSearch = $(R.id.btnSearch);
        rvList = $(R.id.rvList);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {

        }
    }
}
