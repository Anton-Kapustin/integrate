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
        String actionAndroidSend = "android.intent.action.SEND";
        String actionAndroidMultiple = "android.intent.action.SEND_MULTIPLE";
        Log.d(LOG_TAG, "action share: " + action);
        if (action.equals(actionAndroidSend) && type != null) {
            if ("text/plain".equals(type)) {
                Log.d(LOG_TAG, "SHARING TEXT");
                view.passShareToPresenterConnect(sharedText);
//                view.minimazeActivity();
                Log.d(LOG_TAG, "Закртытие активности");
            } else if (type.startsWith("image/")) {
                Log.d(LOG_TAG, "SHARING single image");
            }
        } else if (action.equals(actionAndroidMultiple) && type != null) {
            if (type.startsWith("image/")) {
                Log.d(LOG_TAG, "SHARING multiple image");
            }
        }
    }

    @Override
    public void ActivityOnPause() {

    }

    @Override
    public void ActivityOnDestroy() {

    }

}
