package com.dev.toxa.integrate.FragmentSettings;

import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbHelper;

/**
 * Created by toxa on 16.10.17.
 */
public class ModelFragmentSettings {

    //=============================================Переменные===========================================================

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private DbHelper dbHelper;
    //==================================================================================================================

    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void setNotifyEnabled(boolean enable) {
        dbHelper.addToBaseNotifyEnabled(enable);
    }

    public boolean getNotifyEnabled() {
        boolean enabled = dbHelper.getNotifyEnabled();
        return enabled;
    }
}
