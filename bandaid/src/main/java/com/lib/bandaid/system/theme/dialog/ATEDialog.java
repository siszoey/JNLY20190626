package com.lib.bandaid.system.theme.dialog;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.R;

/**
 * Created by zy on 2019/4/25.
 */

public class ATEDialog extends MaterialDialog {

    protected ATEDialog(Builder builder) {
        super(builder);
    }


    public static class Theme extends Builder {


        public Theme(@NonNull Context context) {
            super(context);

            titleColor(context.getResources().getColor(R.color.colorPrimary));
            contentColor(context.getResources().getColor(R.color.colorAccent));
            negativeColor(Color.BLACK);
            positiveColor(Color.BLACK);

            /*titleColor(Config.primaryColor(context));
            contentColor(Config.textColorPrimary(context));
            negativeColor(Config.textColorSecondary(context));
            positiveColor(Config.textColorSecondary(context));*/
        }
    }

    public static class Theme_Setting extends Builder {


        public Theme_Setting(@NonNull Context context) {
            super(context);

            titleColor(context.getResources().getColor(R.color.colorPrimary));
            contentColor(context.getResources().getColor(R.color.colorAccent));
            negativeColor(Color.BLACK);
            positiveColor(Color.BLACK);

           /* titleColor(Config.primaryColor(context));
            contentColor(Config.textColorPrimary(context));
            negativeColor(Config.textColorSecondary(context));
            positiveColor(Config.textColorSecondary(context));*/
            iconRes(R.drawable.ic_setting);
        }
    }

    public static class Theme_Alert extends Builder {


        public Theme_Alert(@NonNull Context context) {
            super(context);

            titleColor(context.getResources().getColor(R.color.colorPrimary));
            contentColor(context.getResources().getColor(R.color.colorAccent));
            negativeColor(Color.BLACK);
            positiveColor(Color.BLACK);

           /* titleColor(Config.primaryColor(context));
            contentColor(Config.textColorPrimary(context));
            negativeColor(Config.textColorSecondary(context));
            positiveColor(Config.textColorSecondary(context));*/
            iconRes(R.drawable.ic_alert);
        }
    }

}
