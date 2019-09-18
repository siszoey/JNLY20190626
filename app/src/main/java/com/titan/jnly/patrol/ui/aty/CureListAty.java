package com.titan.jnly.patrol.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.remote.entity.TTResult;
import com.lib.bandaid.data.remote.listen.NetWorkListen;
import com.lib.bandaid.message.FuncManager;
import com.lib.bandaid.message.FuncWithParamNoResult;
import com.lib.bandaid.system.theme.dialog.ATEDialog;
import com.lib.bandaid.util.OSerial;
import com.lib.bandaid.util.ObjectUtil;
import com.lib.bandaid.util.PageParam;
import com.lib.bandaid.util.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.titan.jnly.R;
import com.titan.jnly.examine.ui.aty.DataScanAty;
import com.titan.jnly.patrol.api.IPatrolCure;
import com.titan.jnly.patrol.apt.CureListApt;
import com.titan.jnly.patrol.bean.CureModel;
import com.titan.jnly.patrol.util.DataConvertUtil;
import com.titan.jnly.system.Constant;

import java.util.List;
import java.util.Map;

public class CureListAty extends BaseMvpCompatAty
        implements View.OnClickListener,
        OnRefreshListener,
        OnLoadMoreListener,
        BaseRecycleAdapter.IViewClickListener<CureModel> {

    protected Map treeData;
    private TextView tvNum, tvOrder, tvName, tvDate, tvAge, tvDetail;
    private RecyclerView rvList;
    private CureListApt cureListApt;
    private SmartRefreshLayout swipeLayout;
    private PageParam pageParam = PageParam.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) treeData = OSerial.getData(getIntent(), Map.class);
        initTitle(R.drawable.ic_back, "养护", Gravity.CENTER);
        setContentView(R.layout.patrol_ui_aty_cure_list_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_right_add).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_right_add) {
            Intent intent = new Intent(_context, CureItemAty.class);
            OSerial.putSerial(intent, treeData);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initialize() {
        tvDetail = $(R.id.tvDetail);
        tvNum = $(R.id.tvNum);
        tvOrder = $(R.id.tvOrder);
        tvName = $(R.id.tvName);
        tvDate = $(R.id.tvDate);
        tvAge = $(R.id.tvAge);
        rvList = $(R.id.rvList);

        swipeLayout = $(R.id.swipeLayout);
    }

    @Override
    protected void registerEvent() {
        tvDetail.setOnClickListener(this);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadMoreListener(this);
        dispatchFunc();
    }

    @Override
    protected void initClass() {
        cureListApt = new CureListApt(rvList);
        cureListApt.setIViewClickListener(this);
        treeInfo();
        queryData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDetail) {
            Intent intent = new Intent(_context, DataScanAty.class);
            OSerial.putSerial(intent, treeData);
            startActivity(intent);
        }
    }

    void treeInfo() {
        if (treeData == null) return;
        String sign = StringUtil.removeNull(treeData.get("DZBQH"));
        pageParam.pushOtherParam("eleCode", sign);
        String order = StringUtil.removeNull(treeData.get("DCSXH"));
        String species = StringUtil.removeNull(treeData.get("SZZWM"));
        String dateStr = StringUtil.removeNull(treeData.get("DCRQ"));
        dateStr = StringUtil.removeNull(dateStr);
        String age = StringUtil.removeNull(treeData.get("MODEL_AGE"));

        tvNum.setText(sign);
        tvOrder.setText(order);
        tvName.setText(species);
        tvDate.setText(dateStr);
        tvAge.setText(age);
    }

    void queryData() {
        String eleCode = pageParam.getOtherParam("eleCode");
        netEasyReq.request(IPatrolCure.class, new NetWorkListen<TTResult<Map>>() {
            @Override
            public void onSuccess(TTResult<Map> data) {
                List<CureModel> list = ObjectUtil.convert(data.getContent().get("rows"), CureModel.class);
                if (pageParam.isNew()) {
                    swipeLayout.finishRefresh();
                    cureListApt.replaceAll(list);
                }
                if (pageParam.isMore()) {
                    swipeLayout.finishLoadMore();
                    cureListApt.appendList(list);
                }
            }

            @Override
            public void onError(int err, String errMsg, Throwable t) {
                if (pageParam.isNew()) {
                    swipeLayout.finishRefresh();
                    cureListApt.removeAll();
                }
                if (pageParam.isMore()) {
                    swipeLayout.finishLoadMore();
                }
            }
        }).httpGetCureList(eleCode, pageParam.getNum(), pageParam.getSize());
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageParam.New();
        queryData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageParam.next();
        queryData();
    }

    public final static String FUNC_CURE_ADD = "FUNC_CURE_ADD";
    public final static String FUNC_CURE_EDIT = "FUNC_CURE_EDIT";

    private void dispatchFunc() {
        FuncManager.getInstance().addFunc(new FuncWithParamNoResult<CureModel>(FUNC_CURE_ADD) {
            @Override
            public void function(CureModel data) {
                cureListApt.preposeItem(data);
            }
        });

        FuncManager.getInstance().addFunc(new FuncWithParamNoResult<CureModel>(FUNC_CURE_EDIT) {
            @Override
            public void function(CureModel data) {
                cureListApt.updateItem(data);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FuncManager.getInstance().removeFunc(FUNC_CURE_ADD, FUNC_CURE_EDIT);
    }

    @Override
    public void onClick(View view, CureModel data, int position) {
        int id = view.getId();
        if (id == R.id.ivEdit) {
            editItem(data);
        }
        if (id == R.id.ivDel) {
            delItem(data, position);
        }
    }

    void editItem(CureModel data) {
        Map map = DataConvertUtil.convertCureSer2Local(data);
        Intent intent = new Intent(_context, CureItemAty.class);
        OSerial.putSerial(intent, map);
        startActivity(intent);
    }

    void delItem(CureModel data, int position) {
        new ATEDialog.Theme_Alert(_context)
                .title("提示")
                .content("确认删除当前信息？")
                .positiveText("删除")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String userId = Constant.getUserInfo().getId();
                        String itemId = (String) data.getMaintainRecord().get("Id");
                        netEasyReq.request(IPatrolCure.class, new NetWorkListen<TTResult<Map>>() {
                            @Override
                            public void onSuccess(TTResult<Map> data) {
                                cureListApt.removeItem(position);
                            }
                        }).httpDelCureItem(userId, itemId);
                    }
                }).show();
    }
}
