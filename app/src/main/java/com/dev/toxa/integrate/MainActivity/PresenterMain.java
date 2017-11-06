package com.dev.toxa.integrate.MainActivity;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbHelper;

public class PresenterMain implements MVPmain.presenter{

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    ModelMain modelMain;
    private final MVPmain.view view;

    public PresenterMain(MVPmain.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        this.view = view;
        modelMain = new ModelMain();
    }


    @Override
    public void activityLoaded() {
        DbHelper dbHelper = new DbHelper(view.getContext(), 1);
    }

    @Override
    public void ActivityOnResume(String action, String type, String sharedText) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
      }

    @Override
    public void ActivityOnPause() {

    }

    @Override
    public void ActivityOnDestroy() {

    }

}
