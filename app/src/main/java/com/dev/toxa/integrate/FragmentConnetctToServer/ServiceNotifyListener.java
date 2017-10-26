package com.dev.toxa.integrate.FragmentConnetctToServer;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;

public class ServiceNotifyListener extends NotificationListenerService {


    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    //==========================================Переменные==============================================================
    Context context;
    CallbackToPressenter presenter;

    NotifyServiceBinder binder = new NotifyServiceBinder();

    String action = "android.service.notification.NotificationListenerService";

    //==================================================================================================================

    public ServiceNotifyListener() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "Notify Service создан");
    }

    public void setPresenter(PresenterFragmentConnectToServer presenter) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        this.presenter = presenter;
    }

    @Override
    public void onCreate() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onListenerConnected() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void onListenerDisconnected() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        action = intent.getAction();
        if (action != null) {
            return super.onBind(intent);
        } else {
            return binder;
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (presenter != null) {
            String appName = statusBarNotification.getPackageName();
            String key = statusBarNotification.getKey();
            try {
                String text = statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString();
                String title = statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();
                Log.d(LOG_TAG, "name: " + appName);
                Log.d(LOG_TAG, "title: " + title);
                Log.d(LOG_TAG, "text: " + text);
                if (appName.equals("android")) {
                    presenter.notifyPosted(appName, title, text);
                } else {
                    String[] applicationName = appName.split("\\.");
                    int index = applicationName.length -1;
                    if (applicationName[index].contains("dialer")) {
                        presenter.calling(applicationName[index], title, text);
                    } else {
                        presenter.notifyPosted(applicationName[index], title, text);
                    }
                }

            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "NULL error: " + e);
            }
        } else {
            Log.d(LOG_TAG, "Presenter null");
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification){
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "Message remove");
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "Notification Service Destroyed");
        super.onDestroy();
    }

    interface CallbackToPressenter {
        void notifyPosted(String appName, String title, String text);
        void calling(String appName, String title, String text);
    }

    public class NotifyServiceBinder extends Binder {
        public ServiceNotifyListener getService() {
            return ServiceNotifyListener.this;
        }
    }
}
