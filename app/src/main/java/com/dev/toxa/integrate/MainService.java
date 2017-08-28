package com.dev.toxa.integrate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.util.Log;

public class MainService extends Service {
    String LOG_TAG = "MainService";
    serviceBroadcastReceiver receiver;
    String actionRestartService = "com.dev.toxa.integrate.ClientConnect.actionRestartService";
    Intent statusHwService;
    Intent statusPhoneHwService;
    Intent commandService;
    Intent notifyListenerService;

    public MainService(){
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service Создан");

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new serviceBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        Log.d(LOG_TAG, "onStartCommand");
        statusHwService = new Intent(MainService.this, StatusHwService.class);
        statusPhoneHwService = new Intent(MainService.this, StatusPhoneHwService.class);
        commandService = new Intent(MainService.this, CommandService.class);
        notifyListenerService = new Intent(MainService.this, NotifyListenerService.class);
        statusHwService.putExtra("parameters", "firstRun");
        startService(statusHwService);
        startService(commandService);
        startService(statusPhoneHwService);
        startService(notifyListenerService);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
//        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        Log.d(LOG_TAG, "Main Service остановлен");
        super.onDestroy();
    }

    public class serviceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.contains("TIME_TICK")) {
                Log.i(LOG_TAG, "Broadcast in MainService: " + action);
                statusHwService.putExtra("parameters", "RunFromReceiver");
                startService(statusHwService);
            } else if (action.contains("RestartService")) {
                Log.i(LOG_TAG, "Broadcast in MainService: " + action);
                startService(statusHwService);
            }
        }
    }
}


