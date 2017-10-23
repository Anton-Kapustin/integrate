package com.dev.toxa.integrate.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import com.dev.toxa.integrate.CommandService;
import com.dev.toxa.integrate.LoggingNameClass;

public class MainService extends Service {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    serviceBroadcastReceiver receiver;

    String actionToMainService = "com.dev.toxa.integrate.Services.MainService";
    String IP = null;
    String parameters = "parameters";

    Intent statusHwService;
    Intent statusPhoneHwService;
    Intent commandService;
    Intent notifyListenerService;
    Intent serverSearchService;

    public MainService(){
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service Создан");

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new serviceBroadcastReceiver();
//        registerReceiver(receiver, new IntentFilter(actionMainServiceConnect));
//        registerReceiver(receiver, new IntentFilter(actionRestartService));
//        registerReceiver(receiver, new IntentFilter(actionToMainServicePhoneInfo));
        statusHwService = new Intent(MainService.this, StatusHwService.class);
        statusPhoneHwService = new Intent(MainService.this, StatusPhoneHwService.class);
        commandService = new Intent(MainService.this, CommandService.class);
        notifyListenerService = new Intent(MainService.this, NotifyListenerService.class);
        registerReceiver(receiver, new IntentFilter(actionToMainService));
        serverSearchService = new Intent(MainService.this, ServerSearchService.class);
        startService(serverSearchService);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    public void connectToServer(Intent broadcastIntent) {
        broadcastIntent.setClass(MainService.this, StatusHwService.class);
        broadcastIntent.putExtra("IP", IP);
        Log.d(LOG_TAG, "IP: " + IP);
        startService(broadcastIntent);
    }

    public void connectToServer() {
        statusHwService.putExtra("IP", IP);
        Log.d(LOG_TAG, "IP: " + IP);
        startService(statusHwService);
    }

    public void startServices(){
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        Log.d(LOG_TAG, "Запуск сервисов");
        statusHwService.putExtra("parameters", "connect");
        statusHwService.putExtra("IP", IP);
        Log.d(LOG_TAG, "Запуск сервисов");
        Log.d(LOG_TAG, "IP: " + IP);
        startService(statusHwService);
        startService(commandService);
        startService(statusPhoneHwService);
        startService(notifyListenerService);
    }

    @Override
    public void onDestroy() {
//        unregisterReceiver(receiver);
        Log.d(LOG_TAG, "Main Service остановлен");
        super.onDestroy();
    }

    public class serviceBroadcastReceiver extends BroadcastReceiver {
        String parameters = "parameters";
        int count = 0;
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                String params = intent.getStringExtra(parameters);
                Log.d(LOG_TAG, "Broadcast in MainService: " + action);
                if (action.contains("TIME_TICK")) {
                    Log.i(LOG_TAG, "Broadcast in MainService: " + action);
                    connectToServer();
                }
                if (params.contains("restart")) {
                    Log.d(LOG_TAG, "Параметры для заруска: " + params);
                    Log.d(LOG_TAG, "Попытки перезапуска: " + count);
                    if (count < 6) {
                        Log.i(LOG_TAG, "Broadcast в MainService: Перезапуск StatusHwService");
                        connectToServer();
                        count++;
                    }
                } else if (params.contains("selectServer")) {
                    Log.d(LOG_TAG, "Параметры для заруска: " + params);
                    IP = intent.getStringExtra("ip");
                    Log.d(LOG_TAG, "Подготовка клиента для подключения к :" + IP);
                    count = 0;
                    startServices();
                } else if (params.contains("backlight") || params.contains("sound") || params.contains("notify") || params.contains("phone_info") || params.contains("share")) {
                    Log.d(LOG_TAG, "Параметры для заруска: " + params);
                    connectToServer(intent);
                }
            } catch (NullPointerException e) {
            Log.e(LOG_TAG, "onReceive error" + e);
            }
        }
    }
}


