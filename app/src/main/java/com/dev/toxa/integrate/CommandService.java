package com.dev.toxa.integrate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CommandService extends Service {
    String LOG_TAG = "commandService";
    String receiveAction = "com.dev.toxa.integrate.StatusHwPhoneState.Info";
    String sendAction = "com.dev.toxa.integrate.commandService.Info.Send";
    String actionToCommnandService = "com.dev.toxa.integrate.CommnadService.backlight";
    String actionToCommnandServiceSound = "com.dev.toxa.integrate.CommnadService.sound";
    String actionNotify = "com.dev.toxa.integrate.NotifyListenerService.Notify";

    String backlight = "backlight";
    CommandBroadcastReceiver receiver;
    public CommandService() {
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Command Service Создан");

    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Command Service  запущен");
        receiver = new CommandBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(receiveAction));
        registerReceiver(receiver, new IntentFilter(actionToCommnandService));
        registerReceiver(receiver, new IntentFilter(actionToCommnandServiceSound));
        registerReceiver(receiver, new IntentFilter(actionNotify));
        Log.d(LOG_TAG, "Приемник создан");
        return START_NOT_STICKY;

    }

    public void getStatus () {
        Intent statusHwService = new Intent(CommandService.this, StatusHwService.class);
        statusHwService.setAction(sendAction);
        statusHwService.putExtra("parameters", "phoneInfo");
        statusHwService.putExtra("bat", getBatteryState());
        statusHwService.putExtra("net", getNetworkState());
//        Bundle bundle = new Bundle();
//        bundle.putString("battery", getBatteryState());
//        statusHwService.putExtras(bundle);
        Log.d(LOG_TAG, "Запуск hwStatus");
        Log.d(LOG_TAG, "" + statusHwService.getStringExtra("parameters"));
        startService(statusHwService);

    }

    public String getBatteryState() {
        Log.d(LOG_TAG, "Пролучение данных о батерее");
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String status = "battery ";
        if (bm.isCharging()) {
            status += "charging ";
        }
        status += String.valueOf(level) + "%";
        Log.d(LOG_TAG, "Батарея телефона : " + status);
        return status;
    }

    public String getNetworkState() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();
        String networkStatus = "network " + carrierName;
//        ConnectivityManager conmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        conmgr.
        Log.d(LOG_TAG, "carrierName: " + carrierName);
        return networkStatus;
    }


    @Override
    public IBinder onBind(Intent intent){
        return null;
//        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        Log.d(LOG_TAG, "Command Service остановлен");
        super.onDestroy();
    }

    public class CommandBroadcastReceiver extends BroadcastReceiver {
        String parameters = "parameters";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Broadcast in Command service ");
            String action = intent.getAction();
            Intent statusHwService = new Intent(CommandService.this, StatusHwService.class);
            statusHwService.setAction(sendAction);
            if (action.contains("backlight")) {
                int i = 9;
                int back = intent.getIntExtra("backlight", i);
                Log.d(LOG_TAG, "seekbar: " + back);
                statusHwService.putExtra(parameters, "backlight");
                statusHwService.putExtra("light", back);
                startService(statusHwService);
            } else if (action.contains("sound")) {
                String snd = "sound";
                int i = 9;
                int sound = intent.getIntExtra(snd, i);
                Log.d(LOG_TAG, "seekbar: " + sound);
                statusHwService.putExtra(parameters, snd);
                statusHwService.putExtra("snd", sound);
                startService(statusHwService);
            } else if (action.contains("Notify")) {
                String name = intent.getStringExtra("name");
                String title = intent.getStringExtra("title");
                String text = intent.getStringExtra("text");
                String notify = "name: " + name + "/ " + "title: " + title + "/ " + "text: " + text;
                statusHwService.putExtra(parameters, "notification");
                statusHwService.putExtra("notify", notify);
                startService(statusHwService);
            }
            else {
                getStatus();
            }
        }
    }
}
