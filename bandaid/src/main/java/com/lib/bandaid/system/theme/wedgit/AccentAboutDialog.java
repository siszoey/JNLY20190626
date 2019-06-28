package com.lib.bandaid.system.theme.wedgit;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.text.Html;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lib.bandaid.R;
import com.lib.bandaid.system.theme.utils.Config;


/**
 * @author Aidan Follestad (afollestad)
 */
public class AccentAboutDialog extends DialogFragment {

    public static void show(AppCompatActivity context) {
        AccentAboutDialog dialog = new AccentAboutDialog();
        dialog.show(context.getSupportFragmentManager(), "[ABOUT_DIALOG]");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity context = getActivity();
        final int accentColor = Config.accentColor(context);
        return new MaterialDialog.Builder(context)
                .title(R.string.about)
                .positiveText(R.string.dismiss)
                .titleColor(Config.primaryColor(context))
                .contentColor(Config.textColorSecondary(context))
                .linkColor(accentColor)
                .buttonRippleColor(accentColor)
                .positiveColor(accentColor)
                .content(Html.fromHtml(getString(R.string.about_body)))
                .contentLineSpacing(1.6f)
                .build();
    }
}