package com.dev.toxa.integrate.ActivitySharing;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;

import java.util.Observable;

public class ObservableShare extends Observable {

//=================================Переменные==============================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";
    private static volatile ObservableShare instance = null;
//=========================================================================

    public ObservableShare(){
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    public static ObservableShare getInstance() {
        Log.i("OBSERVABLE: ", "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (instance == null) {
            instance = new ObservableShare();
        }
        return instance;
    }

    public void setShareChanged(String shareChanged) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        setChanged();
        notifyObservers(shareChanged);
    }
}
