package com.dev.toxa.integrate.FragmentSettings;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbHelper;

public class PresenterFragmentSettings implements MVPFragmentSettings.presenter {

    //=============================================Переменные===========================================================

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private ModelFragmentSettings modelFragmentSettings = new ModelFragmentSettings();
    private MVPFragmentSettings.view view;
    DbHelper dbHelper;
    //==================================================================================================================

    public PresenterFragmentSettings(MVPFragmentSettings.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        this.view = view;
    }

    @Override
    public void fragmentLoaded() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        dbHelper = new DbHelper(view.getFragmentContext(), 1);
        modelFragmentSettings.setDbHelper(dbHelper);
        settingNotification();
    }

    private void settingNotification() {
        // Усли есть доступ к уведомлениям
        if (view.checkNotificationPermission()) {

            boolean checkboxNotify = modelFragmentSettings.getNotifyEnabled();
            Log.d(LOG_TAG, "notify is enabled " + checkboxNotify);
            view.setCheckbox(checkboxNotify);
        } else {
            view.setCheckbox(false);
            view.openSystemNotifySettings();
        }
    }


    @Override
    public void setView(MVPFragmentSettings.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        this.view = view;
    }

    @Override
    public void checkboxChanged(boolean enabled) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (view.checkNotificationPermission()) {
            modelFragmentSettings.setNotifyEnabled(enabled);
        } else {
            modelFragmentSettings.setNotifyEnabled(false);
            view.setCheckbox(false);
            view.openSystemNotifySettings();
        }
    }
}
