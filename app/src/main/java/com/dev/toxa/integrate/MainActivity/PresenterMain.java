package com.dev.toxa.integrate.MainActivity;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;

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

    }

    @Override
    public void OnResume(String actionSend, String actionMultiple, String actionReceive, String typeReceive) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (actionSend.equals(actionReceive) && typeReceive != null) {
            if ("text/plain".equals(typeReceive)) {
                Log.d(LOG_TAG, "SHARING TEXT");
                view.getShareLink();
//                view.minimazeActivity();
                Log.d(LOG_TAG, "Закртытие активности");
            } else if (typeReceive.startsWith("image/")) {
                Log.d(LOG_TAG, "SHARING single image");
            }
//        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//            if (type.startsWith("image/")) {
//                Log.d(LOG_TAG, "SHARING multiple image");
//            }
        }
    }

    @Override
    public void OnPause() {

    }

    @Override
    public void OnDestroy() {

    }

}
