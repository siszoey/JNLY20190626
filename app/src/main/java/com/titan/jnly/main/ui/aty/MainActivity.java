package com.titan.jnly.main.ui.aty;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.lib.bandaid.permission.Permission;
import com.lib.bandaid.permission.RxConsumer;
import com.lib.bandaid.permission.RxPermissionFactory;
import com.lib.bandaid.permission.SimplePermission;
import com.lib.bandaid.rw.file.utils.FileUtil;
import com.lib.bandaid.service.imp.LocService;
import com.lib.bandaid.utils.PositionUtil;
import com.titan.jnly.Config;
import com.titan.jnly.R;
import com.titan.jnly.common.activity.BaseFragmentAty;
import com.titan.jnly.common.fragment.BaseMainFragment;
import com.titan.jnly.main.ui.frg.decision.DecisionFragment;
import com.titan.jnly.main.ui.frg.manage.ManagerFragment;
import com.titan.jnly.main.ui.frg.my.MyFragment;
import com.titan.jnly.main.ui.frg.watch.WatchFragment;
import com.titan.jnly.system.version.bugly.BuglySetting;

import me.yokeyword.fragmentation.common.SupportFragment;

public class MainActivity extends BaseFragmentAty
        implements BottomNavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnBackToFirstListener, PositionUtil.ILocStatus {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    private BottomNavigationView navigation;
    private SupportFragment[] fragments;
    private int prePosition = FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_menu, "济南名木", Gravity.CENTER);
        setContentView(R.layout.main_ui_aty_main_layout);
        //检查更新
        BuglySetting.checkVersion();
        //权限
        permissions();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            return true;
        }
        return false;
    }

    @Override
    protected void initialize() {
        navigation = $(R.id.navigation);
    }

    @Override
    protected void registerEvent() {
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initClass() {
        fragments = new SupportFragment[4];
        SupportFragment firstFragment = findFragment(DecisionFragment.class);
        if (firstFragment == null) {
            fragments[FIRST] = DecisionFragment.newInstance();
            fragments[SECOND] = ManagerFragment.newInstance();
            fragments[THIRD] = WatchFragment.newInstance();
            fragments[FOURTH] = MyFragment.newInstance();
            loadMultipleRootFragment(R.id.framePage, FIRST, fragments[FIRST], fragments[SECOND], fragments[THIRD], fragments[FOURTH]);
        } else {
            fragments[FIRST] = firstFragment;
            fragments[SECOND] = findFragment(ManagerFragment.class);
            fragments[THIRD] = findFragment(WatchFragment.class);
            fragments[FOURTH] = findFragment(MyFragment.class);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int position = 0;
        switch (item.getItemId()) {
            case R.id.navi_decision:
                position = FIRST;
                break;
            case R.id.navi_manage:
                position = SECOND;
                break;
            case R.id.navi_watch:
                position = THIRD;
                break;
            case R.id.navi_my:
                position = FOURTH;
                break;
        }
        showHideFragment(fragments[position], fragments[prePosition]);
        prePosition = position;
        return true;
    }

    @Override
    public void onBackToFirstFragment() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PositionUtil.resGps(this, requestCode, LocService.class, this);
    }

    @Override
    public void agree() {

    }

    @Override
    public void refuse() {
        finish();
    }

    void permissions() {
        PositionUtil.reqGps(_context, LocService.class, this);
        RxPermissionFactory
                .getRxPermissions(_context)
                .requestEachCombined(SimplePermission.MANIFEST_STORAGE)
                .subscribe(new RxConsumer(_context) {
                    @Override
                    public void accept(Permission permission) {
                        super.accept(permission);
                        if (permission.granted) {
                            FileUtil.createFileSmart(Config.APP_DB_PATH,
                                    Config.APP_SDB_PATH,
                                    Config.APP_MAP_CACHE,
                                    Config.APP_PATH_CRASH,
                                    Config.APP_PHOTO_DIR
                            );
                        } else {
                            finish();
                        }
                    }
                });
    }
}
