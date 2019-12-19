package com.titan.jnly.patrolv1.ui.aty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.titan.jnly.R;
import com.titan.jnly.patrolv1.ui.frg.ConserveTaskListFrg;
import com.titan.jnly.patrolv1.ui.frg.PatrolTaskListFrg;


public class PCMainAty extends BaseMvpCompatAty implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "养护巡查", Gravity.CENTER);
        setContentView(R.layout.patrolv1_conserve_main_layout);
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
        fragments = new Fragment[2];
        fragments[0] = PatrolTaskListFrg.newInstance();
        fragments[1] = ConserveTaskListFrg.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.framePage, fragments[0]).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        navigationItemSelected(menuItem);
        return true;
    }


    private void navigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.navi_decision:
                fragment = fragments[0];
                break;
            case R.id.navi_manage:
                fragment = fragments[1];
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framePage, fragment).commit();
        }
    }

}
