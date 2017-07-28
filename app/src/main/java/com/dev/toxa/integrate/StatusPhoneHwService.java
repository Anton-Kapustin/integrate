package com.dev.toxa.integrate;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;

public class StatusPhoneHwService extends IntentService {
    final String LOG_TAG = "StatusPhoneHwService";
    String receiveAction = "com.dev.toxa.integrate.HwPhoneState.Info.Send";
    Intent infoIntent = new Intent("com.dev.toxa.integrate.HwPhoneState.Info");
    HwPhoneBroadcast hwPhoneBroadcast;
    ServerConnect server = null;

    public StatusPhoneHwService() {
        super("StatusPhoneHwService");
    }

    public static void startAction(Context context) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        hwPhoneBroadcast = new HwPhoneBroadcast();
        registerReceiver(hwPhoneBroadcast, new IntentFilter(receiveAction));
        boolean connection = false;
        String cmd = null;
        int port = 8030;
        int count = 0;
        while (true) {
            try {
                server = new ServerConnect(port);
                server.connect();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Server Connect error");
                break;
            }
            if (server.getInput() == null) {
                continue;
            } else {
                while (server.getInput() != null) {
                    Log.d(LOG_TAG, "Получение комманд");
                    try {
                        cmd = server.receive();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Server receive error");
                    }
                    Log.d(LOG_TAG, "Получено");
                    if (cmd.contains("battery")) {
                        sendBroadcast(infoIntent);
                        Log.d(LOG_TAG, "Broadcast to Command Service receive");
                    }
                }
            }
        }
    }
    public class HwPhoneBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                server.send(intent.getStringExtra(receiveAction));
            } catch (IOException e) {
                Log.e(LOG_TAG, "send error");
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDestroy() {
        unregisterReceiver(hwPhoneBroadcast);
    }
}
