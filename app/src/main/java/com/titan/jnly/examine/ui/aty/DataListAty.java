package com.titan.jnly.examine.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.PageParam;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.widget.edittext.ClearEditText;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.titan.jnly.R;
import com.titan.jnly.examine.apt.DataListApt;
import com.titan.jnly.examine.mvp.Contract;
import com.titan.jnly.examine.mvp.DataListPresenter;
import com.titan.jnly.examine.util.ExamineUtil;
import com.titan.jnly.system.Constant;

import java.util.List;
import java.util.Map;

public class DataListAty extends BaseMvpCompatAty<DataListPresenter>
        implements
        Contract.DataListView,
        OnRefreshListener,
        OnLoadMoreListener,
        BaseRecycleAdapter.IViewClickListener<Map>, View.OnClickListener, ClearEditText.IClearListen {

    private String userId = Constant.getUserInfo().getId();
    private SmartRefreshLayout swipeLayout;
    private DataListApt dataListApt;
    private RecyclerView rvList;
    private PageParam page = PageParam.create();

    private ClearEditText editText;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = new DataListPresenter();
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "审查列表", Gravity.CENTER);
        setContentView(R.layout.exam_ui_aty_data_list_layout);
    }

    @Override
    protected void initialize() {
        rvList = $(R.id.rvList);
        swipeLayout = $(R.id.swipeLayout);
        editText = $(R.id.editText);
        btnSearch = $(R.id.btnSearch);
    }

    @Override
    protected void registerEvent() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadMoreListener(this);
        btnSearch.setOnClickListener(this);
        editText.setClearListen(this);
    }

    @Override
    protected void initClass() {
        dataListApt = new DataListApt(rvList);
        dataListApt.setIViewClickListener(this);
        page.New();
        requestData(userId);
    }

    @Override
    public void getListSuccess(List<Map> list) {
        if (page.isNew()) {
            swipeLayout.finishRefresh();
            dataListApt.replaceAll(list);
        }
        if (page.isMore()) {
            swipeLayout.finishLoadMore();
            dataListApt.appendList(list);
        }
    }

    @Override
    public void getListFail(String msg) {
        if (page.isNew()) swipeLayout.finishRefresh(false);
        if (page.isMore()) swipeLayout.finishLoadMore(false);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page.New();
        requestData(userId);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page.next();
        requestData(userId);
    }

    @Override
    public void onClick(View view, final Map data, int position) {
        if (view.getId() == R.id.ivNav) {
            new ATEDialog.Theme_Alert(_context)
                    .title("提示")
                    .content("确认导航到该树位置？")
                    .positiveText("导航")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Poi end = ExamineUtil.mapConvertPoi(data);
                            AmapNaviPage.getInstance().showRouteActivity(_context, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), null);
                        }
                    }).show();
        } else {
            Intent intent = new Intent(_context, DataScanAty.class);
            intent.putExtra("data", new OSerial<>(data));
            startActivity(intent);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            requestDataByCondition();
        }
    }

    /**
     * @param userId
     */
    void requestData(String userId) {
        presenter.getDataList(page.getNum(), page.getSize(), userId);
    }

    /**
     *
     */
    void requestDataByCondition() {
        Map map = new SimpleMap().push("DZBQH", editText.getText().toString()).push("UserId", userId);
        page.New();
        presenter.postDataList(map);
    }

    @Override
    public void clearListen() {
        page.New();
        requestData(userId);
    }
}
