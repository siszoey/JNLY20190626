package com.lib.bandaid.widget.easyui.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.R;
import com.lib.bandaid.adapter.com.SimpleRvAdapter;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.lib.bandaid.widget.easyui.xml.ItemXml;
import com.lib.bandaid.widget.edittext.ClearEditText;

import java.util.ArrayList;
import java.util.List;

public class SimpleDialog extends BaseDialogFrg
        implements View.OnClickListener,
        SimpleRvAdapter.IFillData<ItemXml>,
        BaseRecycleAdapter.IViewClickListener, ClearEditText.IAfterTextChangedListen {

    private List<ItemXml> values;
    private List<Integer> sel;
    private Boolean isMulti;

    private ClearEditText cetPy;
    private LinearLayout llFooter;
    private RecyclerView rvList;
    private SimpleRvAdapter simpleAdapter;
    private Button btnExit, btnSubmit;
    private List<Integer> lastSel = new ArrayList<>();

    public static <T> SimpleDialog newInstance(List<ItemXml> values, List<Integer> sel, boolean isMulti, ICallBack<T> iCallBack) {
        SimpleDialog fragment = new SimpleDialog();
        fragment.setData(values, sel);
        fragment.setIsMulti(isMulti);
        fragment.setCallBack(iCallBack);
        return fragment;
    }

    private void setData(List<ItemXml> values, List<Integer> sel) {
        this.values = values;
        this.sel = sel;
    }

    public void setIsMulti(Boolean isMulti) {
        this.isMulti = isMulti;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_dialog_layout);
    }

    @Override
    protected void initialize() {
        cetPy = $(R.id.cetPy);
        llFooter = $(R.id.llFooter);
        rvList = $(R.id.rvList);
        btnExit = $(R.id.btnExit);
        btnSubmit = $(R.id.btnSubmit);
    }

    @Override
    protected void registerEvent() {
        btnExit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initClass() {
        if (isMulti) llFooter.setVisibility(View.VISIBLE);
        else llFooter.setVisibility(View.GONE);
        simpleAdapter = new SimpleRvAdapter(rvList, this);
        simpleAdapter.setSelFlags(sel).setMulti(isMulti);
        lastSel.addAll(sel);
        simpleAdapter.replaceAll(values);
        simpleAdapter.setIViewClickListener(this);

        cetPy.inputTypeEnglish();
        cetPy.setIAfterTextChangedListen(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnExit) {
            dismiss();
        }
        if (id == R.id.btnSubmit) {
            dismiss();
            if (iCallBack != null) {
                List<ItemXml> selData = simpleAdapter.getSelData();
                iCallBack.callback(selData);
            }
        }
    }

    @Override
    public String fillData(ItemXml value) {
        return value.getValue();
    }

    @Override
    public void onClick(View view, Object data, int position) {
        if (isMulti) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
                if (iCallBack != null) {
                    List<ItemXml> selData = simpleAdapter.getSelData();
                    iCallBack.callback(selData);
                }
            }
        }, 200);
    }

    @Override
    public void afterTextChanged(int id, String s) {
        simpleAdapter.replaceAll(pyQuery(s));
    }

    List<ItemXml> pyQuery(String text) {
        if (values == null) return null;
        if (ObjectUtil.isEmpty(text)) {
            simpleAdapter.setSelFlags(lastSel);
            return values;
        }

        lastSel.clear();
        lastSel.addAll(simpleAdapter.getSelFlags());
        simpleAdapter.clearSel();

        List<ItemXml> res = new ArrayList<>();
        for (ItemXml itemXml : values) {
            if (itemXml == null) continue;
            if (itemXml.getPy().contains(text.toLowerCase())) {
                res.add(itemXml);
            }
        }
        return res;
    }
}
