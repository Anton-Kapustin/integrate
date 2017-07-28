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
    Intent intentToMain = new Intent("com.dev.toxa.integrate.ClientConnect.Broadcast");
    HwPhoneBroadcast hwPhoneBroadcast;
    ServerConnect server = null;
    ActionsWithFile actions = new ActionsWithFile();
    String batteryFile = "battery";
    String networkFile = "network";
    String state = null;
    String battery = null;
    String network = null;


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
                Log.d(LOG_TAG, "Запуск сервера");
                server = new ServerConnect(port);
                server.connect();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Server Connect error");
            }
            String[] str = new String[2];

            while (server.getInput() != null) {
                Log.d(LOG_TAG, "Получение комманд");
                try {
                    cmd = server.receive();
//                        while ((!(cmd = server.receive()).contains("end"))) {
                        Log.d(LOG_TAG, "Получено");
                    if (cmd.contains("end")){
                        break;
                    } else if (cmd.contains("phone")) {
                        sendBroadcast(infoIntent);
                        Log.d(LOG_TAG, "Broadcast to Command Service receive");
                    } else if (cmd.contains("battery")) {
                        Log.d(LOG_TAG, cmd);
                        str[0] = cmd;
                    } else if (cmd.contentEquals("network")) {
                        Log.d(LOG_TAG, cmd);
                        str[0] = cmd;
                    } else {
                        str[1] = cmd;
                        if(str[0].contains("battery")) {
                            actions.writeInFile(str[1], batteryFile);
                        } else if (str[0].contains("network")) {
                            actions.writeInFile(str[1], networkFile);
                        }
                    }
                } catch (IOException e) {
                    Log.d(LOG_TAG, "server receive error");
                    e.printStackTrace();
                }
            }
            try {
                server.close();
            } catch (IOException e) {
                Log.d(LOG_TAG, "server close error");
                e.printStackTrace();
            }
            Log.d(LOG_TAG,"Соединение разорвано");
            sendBroadcast(intentToMain);
            Log.d(LOG_TAG, "Broadcasts send");
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
        public void onDestroy () {
            unregisterReceiver(hwPhoneBroadcast);
        }
    }

