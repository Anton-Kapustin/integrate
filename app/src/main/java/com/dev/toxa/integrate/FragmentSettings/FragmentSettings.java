package com.dev.toxa.integrate.FragmentSettings;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.MainActivity.MainActivity;
import com.dev.toxa.integrate.R;

public class FragmentSettings extends Fragment implements MVPFragmentSettings.view {

    //=============================================Переменные===========================================================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    CheckBox checkBoxNotify;

    PresenterFragmentSettings presenter;
    MainActivity activity;
    Context context;

    //==================================================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity = (MainActivity) getActivity();
        presenter = activity.setFragmentSettings(this);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        View rootView = inflater.inflate(R.layout.fragment_layout_settings, container, false);
        checkBoxNotify = (CheckBox) rootView.findViewById(R.id.settings_cb_notify_enable);
        checkBoxNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.checkboxChanged(b);
        }
        });

        return rootView;
    }

    @Override
    public Context getFragmentContext() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        return getContext();
    }

    @Override
    public void setCheckbox(boolean enabled) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        checkBoxNotify.setChecked(enabled);
    }


    //===========================================Жизненный цикл=========================================================

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));

        if (savedInstanceState != null) {

        }
        presenter.fragmentLoaded();
    }

    //============================================Доступ к уведомлениям=================================================

    @Override
    public boolean checkNotificationPermission() {
        String enabled = Settings.Secure.getString(activity.getContentResolver(), "enabled_notification_listeners");
        return (enabled != null && enabled.contains(activity.getPackageName()));
    }

    @Override
    public void openSystemNotifySettings() {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);
    }
}
