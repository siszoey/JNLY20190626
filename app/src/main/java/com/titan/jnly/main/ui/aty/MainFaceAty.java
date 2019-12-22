package com.titan.jnly.main.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.util.AppUtil;
import com.lib.bandaid.widget.squareview.GridItem;
import com.lib.bandaid.widget.squareview.LineGridView;
import com.lib.bandaid.widget.squareview.SquareAdapter;
import com.titan.jnly.R;
import com.titan.jnly.common.mvp.DicHandle;
import com.titan.jnly.examine.ui.aty.ExamineAty;
import com.titan.jnly.invest.ui.aty.InvestActivity;
import com.titan.jnly.login.bean.UserInfo;
import com.titan.jnly.login.ui.dlg.DialogMould;
import com.titan.jnly.patrolv1.ui.aty.ConserveTaskListAty;
import com.titan.jnly.patrolv1.ui.aty.PCMainAty;
import com.titan.jnly.pubser.ui.aty.PublicSerAty;
import com.titan.jnly.system.Constant;
import com.titan.jnly.system.version.bugly.BuglySetting;

import java.util.ArrayList;
import java.util.List;

public class MainFaceAty extends BaseMvpCompatAty implements AdapterView.OnItemClickListener {

    private TextView copyRight;
    private LineGridView gridView;
    private List<GridItem> items;
    private SquareAdapter squareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, "济南古树", Gravity.CENTER);
        setContentView(R.layout.main_ui_aty_face_layout);
        //检查更新
        BuglySetting.checkVersion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_sync_dic).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sync_dic) {
            DicHandle.create(_context).reqDic(Constant.getUser());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initialize() {
        gridView = $(R.id.iconParent);
        copyRight = $(R.id.copyRight);
    }

    @Override
    protected void registerEvent() {
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void initClass() {
        String text = "济南市林木种质资源中心\n" + "技术支持:北京航天泰坦(" + AppUtil.getApkVersionName(this) + ")";
        copyRight.setText(text);

        items = new ArrayList<>();
        items.add(GridItem.create(R.mipmap.ic_dc, "调查管理"));
        items.add(GridItem.create(R.mipmap.ic_xc, "巡查养护"));
        items.add(GridItem.create(R.mipmap.ic_zs, "综合展示"));
        items.add(GridItem.create(R.mipmap.ic_fw, "公共服务"));
        items.add(GridItem.create(R.mipmap.ic_jc, "领导决策"));
        squareAdapter = new SquareAdapter(this, items);
        gridView.setAdapter(squareAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfo info = Constant.getUserInfo();

        if (position == 0) {
           /* if (info.getUserRoles().trim().equals("调查员")) {
                startActivity(new Intent(_context, InvestActivity.class));
            } else if (info.getUserRoles().trim().equals("单位审核员")) {
                startActivity(new Intent(_context, ExamineAty.class));
            } else {
                DialogMould.newInstance().show(_context);
            }*/

            if (info.getUserRoles().trim().equals("调查员")) {
                startActivity(new Intent(_context, InvestActivity.class));
            } else if (info.getUserRoles().trim().contains("调查员")
                    || info.getUserRoles().trim().contains("审核员")) {
                DialogMould.newInstance().show(_context);
            } else if (!info.getUserRoles().trim().contains("调查员")
                    && info.getUserRoles().trim().contains("审核员")) {
                startActivity(new Intent(_context, ExamineAty.class));
            } else {
                showToast("权限有问题！");
            }

        } else if (position == 1) {
            //startActivity(new Intent(_context, PatrolCureActivity.class));
            //startActivity(new Intent(_context, PatrolTaskListAty.class));
            startActivity(new Intent(_context, PCMainAty.class));
        } else if (position == 2) {
            startActivity(new Intent(_context, ConserveTaskListAty.class));
            //showToast("开发中");
        } else if (position == 3) {
            startActivity(new Intent(_context, PublicSerAty.class));
        } else if (position == 4) {
            //startActivity(new Intent(_context, PatrolLogAty.class));
            // startActivity(new Intent(_context, FieldInquireAty.class));
            showToast("二期功能，暂未开放");
        } else {
            showToast("二期功能，暂未开放");
        }
    }
}
