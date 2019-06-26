package com.lib.bandaid.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lib.bandaid.R;

/**
 * Created by zy on 2019/5/13.
 */

public abstract class BaseDialogActivity extends BaseTemplateAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common, menu);
        menu.findItem(R.id.menu_right_close).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_right_close) {
            finish();
        }
        return true;
    }


}
