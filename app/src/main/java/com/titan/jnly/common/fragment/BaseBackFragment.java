package com.titan.jnly.common.fragment;

import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.titan.jnly.R;

import me.yokeyword.fragmentation.common.SupportFragment;


/**
 * Created by YoKeyword on 16/2/7.
 */
public class BaseBackFragment extends SupportFragment {

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // _mActivity.onBackPressed();
            }
        });
    }
}
