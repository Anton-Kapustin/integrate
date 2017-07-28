package com.dev.toxa.integrate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service {
    String LOG_TAG = "MainService";
    serviceBroadcastReceiver receiver;
    Intent StatusHwService;
    Intent StatusPhoneHwService;
    Intent CommandService;

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
        StatusHwService = new Intent(MainService.this, StatusHwService.class);
        StatusPhoneHwService = new Intent(MainService.this, StatusPhoneHwService.class);
        CommandService = new Intent(MainService.this, CommandService.class);

//        startService(StatusHwService);
        startService(CommandService);
        startService(StatusPhoneHwService);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
        // TODO: Return the communication channel to the service.
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
            Log.i(LOG_TAG, "Broadcast in service ");
//            startService(StatusHwService);
        }
    }
}
