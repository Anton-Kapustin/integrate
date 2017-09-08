package com.dev.toxa.integrate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CommandService extends Service {
    String LOG_TAG = "commandService";
    String actionToMainService = "com.dev.toxa.integrate.MainService";
    String actionReceive = "com.dev.toxa.integrate.CommnadService";

    String actionBatteryChanged = Intent.ACTION_BATTERY_CHANGED;
    String backlight = "backlight";
    String parameters = "parameters";
    String batteryCharging = "";

    int voltage = 0;
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
        registerReceiver(receiver, new IntentFilter(actionBatteryChanged));
        registerReceiver(receiver, new IntentFilter(actionReceive));
//        registerReceiver(receiver, new IntentFilter(receiveAction));
//        registerReceiver(receiver, new IntentFilter(actionToCommnandServiceSound));
//        registerReceiver(receiver, new IntentFilter(actionNotify));
        Log.d(LOG_TAG, "Приемник создан");
        return START_NOT_STICKY;

    }

    public void getStatus () {
        Intent intentMainService = new Intent();
        intentMainService.setAction(actionToMainService);
        intentMainService.putExtra(parameters, "phone_info");
        intentMainService.putExtra("bat", getBatteryState());
        intentMainService.putExtra("net", getNetworkState());
        sendBroadcast(intentMainService);
    }

    public String getBatteryState() {
        Log.d(LOG_TAG, "Пролучение данных о батерее");
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String status = "battery ";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (bm.isCharging()) {
//                status += "charging ";
//            }
//        } else {
        status += batteryCharging;
//        }
        Log.d(LOG_TAG, "Статус батареи: " + batteryCharging);
        status += String.valueOf(level) + "%";
        Log.d(LOG_TAG, "Батарея телефона : " + status);
        return status;
    }

    public String getNetworkState() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();
        String networkStatus = "network " + carrierName;
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
            String params = intent.getStringExtra(parameters);
            try {
                if (params.contains("backlight") || params.contains("sound") || params.contains("notify") || params.contains("share")) {
    //                int back = intent.getIntExtra("backlight", -1);
    //                Log.d(LOG_TAG, "seekbar: " + back);
    //                intentToMainService.putExtra(parameters, "backlight");
    //                intentToMainService.putExtra("light", back);
    //                sendBroadcast(intentToMainService);
                    Log.d(LOG_TAG, "Параметры: " + params);
                    intent.setAction(actionToMainService);
                    sendBroadcast(intent);
    //            } else if (params.contains("sound")) {
    ////                String snd = "sound";
    ////                int sound = intent.getIntExtra(snd, -1);
    ////                Log.d(LOG_TAG, "seekbar: " + sound);
    ////                intentToMainService.putExtra(parameters, snd);
    ////                intentToMainService.putExtra("snd", sound);
    ////                sendBroadcast(intentToMainService);
    //                intent.setAction(actionToMainService);
    //                sendBroadcast(intent);
    //            } else if (params.contains("notify")) {
    ////                String name = intent.getStringExtra("name");
    ////                String title = intent.getStringExtra("title");
    ////                String text = intent.getStringExtra("text");
    ////                String notify = "name: " + name + "/ " + "title: " + title + "/ " + "text: " + text;
    ////                intentToMainService.putExtra(parameters, "notification");
    ////                intentToMainService.putExtra("notify", notify);
    ////                Log.d(LOG_TAG, intentToMainService.getStringExtra("notify"));
    ////                sendBroadcast(intentToMainService);
    //                intent.setAction(actionToMainService);
    //                sendBroadcast(intent);
                } else if (params.contains("phone")) {
                    getStatus();
                }
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "null error: " + e);
            }
            if (action.contains(actionBatteryChanged)) {
                voltage = intent.getIntExtra("voltage", -1);
                Log.d(LOG_TAG, "Voltage: " + voltage);
                int batState = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                if (batState == 2) {
                    batteryCharging = "charging ";
                }
                Log.d(LOG_TAG, "battery: " + batteryCharging);
            }
        }
    }
}
