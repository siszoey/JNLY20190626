package com.titan.jnly.examine.ui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.com.NetEasyFactory;
import com.lib.bandaid.data.remote.com.NetEasyReq;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.lib.bandaid.widget.edittext.ClearEditText;
import com.titan.jnly.R;
import com.titan.jnly.examine.api.ExamineApi;
import com.titan.jnly.examine.apt.DataListApt;
import com.titan.jnly.examine.ui.aty.DataScanAty;
import com.titan.jnly.examine.util.ExamineUtil;
import com.titan.jnly.system.Constant;

import java.util.List;
import java.util.Map;

public class NviDialog extends BaseDialogFrg
        implements View.OnClickListener,
        BaseRecycleAdapter.IViewClickListener<Map> {

    private NetEasyReq netEasyReq;
    private ClearEditText editText;
    private Button btnSearch;
    private RecyclerView rvList;
    private DataListApt adapter;
    private String userId = Constant.getUserInfo().getId();

    public static NviDialog newInstance() {
        NviDialog fragment = new NviDialog();
        Bundle bundle = new Bundle();
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
        btnSearch.setOnClickListener(this);
    }

    @Override
    protected void initClass() {
        netEasyReq = NetEasyFactory.createEasy(getContext());
        adapter = new DataListApt(rvList);
        adapter.setIViewClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            queryData();
        }
    }

    public void queryData() {
        Map map = new SimpleMap().push("DZBQH", editText.getText().toString()).push("UserId", userId);
        netEasyReq.request(ExamineApi.class, new NetWorkListen<TTResult<Map>>() {
            @Override
            public void onSuccess(TTResult<Map> data) {
                List<Map> list = ObjectUtil.convert(data.getContent().get("rows"), Map.class);
                adapter.replaceAll(list);
            }
        }).httpPostList(map);
    }

    @Override
    public void onClick(View view, Map data, int position) {
        if (view.getId() == R.id.ivNav) {
            new ATEDialog.Theme_Alert(context)
                    .title("提示")
                    .content("确认导航到该树位置？")
                    .positiveText("导航")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Poi end = ExamineUtil.mapConvertPoi(data);
                            AmapNaviPage.getInstance().showRouteActivity(context, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), null);
                        }
                    }).show();
        } else {
            Intent intent = new Intent(context, DataScanAty.class);
            intent.putExtra("data", new OSerial<>(data));
            startActivity(intent);
        }
    }
}
