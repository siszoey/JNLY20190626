package com.titan.jnly.patrol.ui.dlg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.com.NetEasyFactory;
import com.lib.bandaid.data.remote.com.NetEasyReq;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.lib.bandaid.widget.edittext.ClearEditText;
import com.titan.jnly.R;
import com.titan.jnly.examine.api.ExamineApi;
import com.titan.jnly.examine.ui.aty.DataScanAty;
import com.titan.jnly.patrol.apt.TreeListApt;
import com.titan.jnly.patrol.ui.aty.CureListAty;
import com.titan.jnly.patrol.ui.aty.PatrolListAty;
import com.titan.jnly.system.Constant;

import java.util.List;
import java.util.Map;

public class SearchDialog extends BaseDialogFrg
        implements View.OnClickListener,
        BaseRecycleAdapter.IViewClickListener<Map> {

    private NetEasyReq netEasyReq;
    private ClearEditText editText;
    private Button btnSearch;
    private RecyclerView rvList;
    private TreeListApt adapter;
    private String userId = Constant.getUserInfo().getId();

    public static SearchDialog newInstance() {
        SearchDialog fragment = new SearchDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patrol_ui_dialog_search);
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
        adapter = new TreeListApt(rvList);
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
    public void onClick(View view, final Map data, int position) {
        //巡查
        if (view.getId() == R.id.ivPatrol) {
            Intent intent = new Intent(context, PatrolListAty.class);
            OSerial.putSerial(intent, data);
            startActivity(intent);
            /*new ATEDialog.Theme_Alert(context)
                    .title("提示")
                    .content("确认添加巡查信息？")
                    .positiveText("添加")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(context, PatrolListAty.class);
                            OSerial.putSerial(intent, data);
                            startActivity(intent);
                        }
                    }).show();*/
        }
        //养护
        else if (view.getId() == R.id.ivCure) {
            Intent intent = new Intent(context, CureListAty.class);
            OSerial.putSerial(intent, data);
            startActivity(intent);
           /* new ATEDialog.Theme_Alert(context)
                    .title("提示")
                    .content("确认添加养护信息？")
                    .positiveText("添加")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(context, CureListAty.class);
                            OSerial.putSerial(intent, data);
                            startActivity(intent);
                        }
                    }).show();*/
        } else {
            Intent intent = new Intent(context, DataScanAty.class);
            OSerial.putSerial(intent, data);
            startActivity(intent);
        }
    }
}
