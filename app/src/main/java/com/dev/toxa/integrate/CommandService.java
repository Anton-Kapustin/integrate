package com.dev.toxa.integrate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

public class CommandService extends Service {
    String LOG_TAG = "CommandService";
    String receiveAction = "com.dev.toxa.integrate.HwPhoneState.Info";
    String sendAction = "com.dev.toxa.Integrate.HwPhoneState.Info.Send";
    CommandBroadcastReceiver receiver;
    public CommandService() {
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Command Service Создан");
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        receiver = new CommandBroadcastReceiver();
        registerReceiver(receiver, new IntentFilter(receiveAction));
        return START_NOT_STICKY;

    }

    public void getBatteryStatus () {
        Intent sendIntent = new Intent(sendAction);
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String status = String.valueOf(level) + "%";
        sendIntent.putExtra("Battery", status);
        sendBroadcast(sendIntent);
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

    public class CommandBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG, "Broadcast in Command service ");
            getBatteryStatus();
        }
    }
}
