package com.titan.jnly.pubser.ui.aty;

import android.os.Bundle;
import android.view.Gravity;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lib.bandaid.activity.BaseMvpCompatAty;
import com.lib.bandaid.adapter.BaseFragmentAdapter;
import com.lib.bandaid.fragment.BaseFragment;
import com.titan.jnly.R;
import com.titan.jnly.pubser.ui.frg.PubSerFrg;

import java.util.ArrayList;
import java.util.List;

public class PublicSerAty extends BaseMvpCompatAty {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(R.drawable.ic_back, "公共服务", Gravity.CENTER);
        setContentView(R.layout.pubser_ui_aty_main_layout);
    }

    @Override
    protected void initialize() {
        tabLayout = $(R.id.tabLayout);
        viewPager = $(R.id.viewPager);
    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {
        fragments = new ArrayList<>();
        fragments.add(PubSerFrg.newInstance("政策法规", 1));
        fragments.add(PubSerFrg.newInstance("人文历史", 2));
        fragments.add(PubSerFrg.newInstance("古树保护", 3));
        fragments.add(PubSerFrg.newInstance("标准规法", 4));
        fragments.add(PubSerFrg.newInstance("古树专家", 5));
        fragments.add(PubSerFrg.newInstance("主题活动", 6));
        fragments.add(PubSerFrg.newInstance("交流园地", 7));
        BaseFragmentAdapter.create(viewPager, fragments);
        for (BaseFragment fragment : fragments) {
            tabLayout.addTab(tabLayout.newTab().setText(fragment.getName()));
        }
        tabLayout.setupWithViewPager(viewPager);
    }
}
