package com.dev.toxa.integrate;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotifyListenerService extends NotificationListenerService {

    String LOG_TAG = " NotifylistenService: ";
    Context context;
    String actionToCommnandService = "com.dev.toxa.integrate.CommnadService";
    public NotifyListenerService() {
        Log.d(LOG_TAG, "Notify Service создан");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return super.onBind(intent);
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        Intent notifySend = new Intent(actionToCommnandService);
        String appName = statusBarNotification.getPackageName();
        String key = statusBarNotification.getKey();
        try {
            String text = statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString();
            String title = statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();
//        String[] name = appName.split(".");
//        int index = name.length - 1;
//        appName = name[index];
            Log.d(LOG_TAG, "name: " + appName);
            Log.d(LOG_TAG, "title: " + title);
            Log.d(LOG_TAG, "text: " + text);
            if (appName.contains("android.dialer")) {
                notifySend.putExtra("call", appName);
            } else {
                notifySend.putExtra("name", appName);
            }
            notifySend.putExtra("parameters", "notify");
            notifySend.putExtra("title", title);
            notifySend.putExtra("text", text);
            Log.d(LOG_TAG, "Notify: " + text);
            sendBroadcast(notifySend);
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, "NULL error: " + e);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification){
        Log.d(LOG_TAG, "Message remove");
    }
}
