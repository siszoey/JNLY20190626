package com.lib.bandaid.system.theme.aty;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.lib.bandaid.R;
import com.lib.bandaid.activity.BaseAppCompatAty;
import com.lib.bandaid.system.theme.prefs.ATEColorPreference;
import com.lib.bandaid.system.theme.utils.ATE;
import com.lib.bandaid.system.theme.utils.Config;

/**
 * @author Aidan Follestad (afollestad)
 */
@SuppressLint("NewApi")
public class SettingsActivity extends BaseAppCompatAty implements ColorChooserDialog.ColorCallback {

   static FragmentActivity fragmentActivity;

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        final Config config = ATE.config(this);
        if (dialog.getTitle() == R.string.primary_color) {
            float alpha = Color.valueOf(selectedColor).alpha();
            if (alpha != 1.0) {
                Toast.makeText(SettingsActivity.this, "基色不允许有透明度！", Toast.LENGTH_LONG).show();
                return;
            }
            config.primaryColor(selectedColor);
        }
        if (dialog.getTitle() == R.string.accent_color) {
            config.accentColor(selectedColor);
        }
        if (dialog.getTitle() == R.string.primary_text_color) {
            config.textColorPrimary(selectedColor);
        }
        if (dialog.getTitle() == R.string.secondary_text_color) {
            config.textColorSecondary(selectedColor);
        }
        config.commit();
        recreate(); // recreation needed to reach the checkboxes in the preferences layout
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            ATEColorPreference primaryColorPref = (ATEColorPreference) findPreference("primary_color");
            primaryColorPref.setColor(Config.primaryColor(getActivity()), Color.BLACK);
            primaryColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder(getActivity(), R.string.primary_color)
                            .preselect(Config.primaryColor(getActivity()))
                            .show(fragmentActivity);
                    return true;
                }
            });

            ATEColorPreference accentColorPref = (ATEColorPreference) findPreference("accent_color");
            accentColorPref.setColor(Config.accentColor(getActivity()), Color.BLACK);
            accentColorPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.accent_color)
                            .preselect(Config.accentColor(getActivity()))
                            .show(fragmentActivity);
                    return true;
                }
            });

            ATEColorPreference textColorPrimaryPref = (ATEColorPreference) findPreference("text_primary");
            textColorPrimaryPref.setColor(Config.textColorPrimary(getActivity()), Color.BLACK);
            textColorPrimaryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.primary_text_color)
                            .preselect(Config.textColorPrimary(getActivity()))
                            .show(fragmentActivity);
                    return true;
                }
            });

            ATEColorPreference textColorSecondaryPref = (ATEColorPreference) findPreference("text_secondary");
            textColorSecondaryPref.setColor(Config.textColorSecondary(getActivity()), Color.BLACK);
            textColorSecondaryPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingsActivity) getActivity(), R.string.secondary_text_color)
                            .preselect(Config.textColorSecondary(getActivity()))
                            .show(fragmentActivity);
                    return true;
                }
            });

            findPreference("colored_status_bar").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ATE.config(getActivity())
                            .coloredStatusBar((Boolean) newValue)
                            .apply(getActivity());
                    return true;
                }
            });

            findPreference("colored_nav_bar").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ATE.config(getActivity())
                            .coloredNavigationBar((Boolean) newValue)
                            .apply(getActivity());
                    return true;
                }
            });
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitle(null, "主题", Gravity.CENTER);
        setContentView(R.layout.theme_preference_activity_custom);
        setSupportActionBar((Toolbar) findViewById(R.id.appbar_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentActivity = this;
        if (savedInstanceState == null)
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void registerEvent() {

    }

    @Override
    protected void initClass() {

    }
}