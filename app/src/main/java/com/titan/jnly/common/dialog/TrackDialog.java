package com.titan.jnly.common.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lib.bandaid.adapter.recycle.BaseRecycleAdapter;
import com.lib.bandaid.data.local.sqlite.proxy.transaction.DbManager;
import com.lib.bandaid.service.bean.Loc;
import com.lib.bandaid.thread.rx.RxSimpleUtil;
import com.lib.bandaid.util.SimpleMap;
import com.lib.bandaid.widget.dialog.BaseDialogFrg;
import com.titan.jnly.R;
import com.titan.jnly.common.apt.TrackAdapter;
import com.titan.jnly.system.Constant;

import java.util.List;
import java.util.Map;

public class TrackDialog extends BaseDialogFrg implements BaseRecycleAdapter.IViewClickListener<Map> {


    public static TrackDialog newInstance() {
        TrackDialog fragment = new TrackDialog();
        return fragment;
    }

    public TrackDialog setCallBack(ICallBack iCallBack) {
        this.iCallBack = iCallBack;
        return this;
    }

    RecyclerView rvTrackList;
    TrackAdapter trackAdapter;
    ICallBack iCallBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, "历史轨迹", Gravity.CENTER);
        //iCallBack = (ICallBack) getArguments().getSerializable("data");
        setContentView(R.layout.com_dialog_history_track);
        w = 0.9f;
    }

    @Override
    protected void initialize() {
        rvTrackList = $(R.id.rvTrackList);
    }

    @Override
    protected void registerEvent() {
        trackAdapter = new TrackAdapter(rvTrackList);
        trackAdapter.setIViewClickListener(this);
    }

    @Override
    protected void initClass() {
        queryTrack();
    }

    @Override
    public void onClick(View view, Map data, int position) {
        System.out.println(data);
        String trackId = data.get("trackId") == null ? null : data.get("trackId").toString();
        queryTrackSpot(trackId);
    }

    /**
     * 查询历史轨迹
     */
    void queryTrack() {
        RxSimpleUtil.simple(this, new RxSimpleUtil.ISimpleBack<List<Map>>() {

            @Override
            public List<Map> run() {
                if (Constant.getUser() == null) return null;
                String userID = Constant.getUser().getName();
                String sql = "select count(F_TRACK_ID) as count,min(F_TIME) as startTime,max(F_TIME) as endTime,F_TRACK_ID as trackId from TB_LOCATION where F_USER_ID = '" + userID + "' GROUP BY F_TRACK_ID";
                List<Map> res = DbManager.createDefault().getListMapBySql(sql);
                return res;
            }

            @Override
            public void success(List<Map> res) {
                trackAdapter.appendList(res);
            }
        });
    }

    /**
     * 查询历史轨迹
     */
    void queryTrackSpot(String trackId) {
        RxSimpleUtil.simple(this, new RxSimpleUtil.ISimpleBack<List<Loc>>() {

            @Override
            public List<Loc> run() {
                List<Loc> res = DbManager.createDefault().getListTByMultiCondition(Loc.class, new SimpleMap<>().push("F_TRACK_ID", trackId));
                return res;
            }

            @Override
            public void success(List<Loc> res) {
                dismiss();
                if (iCallBack != null) iCallBack.sure(res);
            }
        });
    }

    public interface ICallBack {
        public void sure(List<Loc> res);
    }
}
