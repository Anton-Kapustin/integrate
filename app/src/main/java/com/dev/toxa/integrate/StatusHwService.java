package com.dev.toxa.integrate;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Array;

public class StatusHwService extends IntentService{
    final String LOG_TAG = "statusHwService";
    final int port = 18030;

    String actionToMainService = "com.dev.toxa.integrate.MainService";

    Intent intentToMainService = new Intent(actionToMainService);

    String batteryFile = "battery";
    String networkFile = "network";
    String backlight = "backlight";
    String phoneInfo = "phoneInfo";
    String parameters = "parameters";
    String state = null;
    String connection = "connection";
    String status = "status";

    ClientConnect clientConnect;

    final ActionsWithFile actions = new ActionsWithFile();

    public StatusHwService(){
        super("statusHwService");
        Log.d(LOG_TAG, "statusHwService Создан");
    }

    public static void startAction(Context context) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String action = intent.getAction();
            String params = intent.getStringExtra("parameters");
            String ip = intent.getStringExtra("IP");
            Log.d(LOG_TAG, "IP: " + ip);
            Log.d(LOG_TAG, "Запуск клиента");
            clientConnect = new ClientConnect();
            clientConnect.connect(ip, port);
            if (clientConnect.getConnectionState()) {
                try {
                    Log.d(LOG_TAG, params);
                    if (params.contains("backlight")) {
                        Log.d(LOG_TAG, "2");
                        int back = intent.getIntExtra("data", -1);
                        String backlightState = backlight + "//// " + String.valueOf(back);
                        clientConnect.send(backlightState);
                        
                        close();
                    }
                } catch (NullPointerException e) {
                    Log.d(LOG_TAG, "Нет параметра " + backlight);
                }
                try {
                    if (params.contains("phone_info")) {
                        String status = "phone_info////" + intent.getStringExtra("bat");
                        status += "&";
                        status += intent.getStringExtra("net");
                        Log.d(LOG_TAG, "Отправка: " + status.toString());
                        clientConnect.send(status);
                        
                        Log.d(LOG_TAG, "Отправлено");
                        close();
                    }
                } catch (NullPointerException e) {
                    Log.d(LOG_TAG, "Нет параметра " + phoneInfo);
                }
                try {
                    if (params.contains("sound")) {
                        String soundState = String.valueOf(intent.getIntExtra("data", -1));
                        String status = "sound//// " + soundState;
                        Log.d(LOG_TAG, "Отправка: " + status);
                        clientConnect.send(status);
                        
                        Log.d(LOG_TAG, "Отправлено");
                        close();
                    }
                } catch (NullPointerException e) {
                    Log.d(LOG_TAG, "Нет параметра " + "sound");
                }
                try {
                    if (params.contains("notify")) {
                        String name = intent.getStringExtra("name");
                        String title = intent.getStringExtra("title");
                        String text = intent.getStringExtra("text");
                        String notify = "notify////name: " + name + "/ " + "title: " + title + "/ " + "text: " + text;
                        clientConnect.send(notify);
                        
                        close();
                    }
                } catch (NullPointerException e) {
                    Log.d(LOG_TAG, "Нет параметра " + "notify");
                }
                try {
                    if (params.contains("share")) {
                        String share = "share_";
                        Bundle bundle = new Bundle();
                        bundle = intent.getExtras();
                        for (String key : bundle.keySet()) {
                            Log.d(LOG_TAG, "key: " + key);
                            if (key.equals("link")) {
                                share += key + "////" + bundle.getString(key);
                            } else if (key.equals("text")) {
                                share += key + "////" + bundle.getString(key);
                            }
                        }
                        clientConnect.send(share);
                        close();

                    }
                } catch (NullPointerException e) {
                    Log.d(LOG_TAG, "Нет параметра " + "share");
                }
                if (params.contains("connect")) {
                    Log.d(LOG_TAG, "Не содержит");
                    clientConnect.send("info");
                    
                    Log.d(LOG_TAG, "Сообщение отправлено");
                    close();
                }
            }
            clientConnect.close();
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "IP error: " + e);
            intentToMainService.putExtra("parameters", "restart");
            sendBroadcast(intentToMainService);
            Log.e(LOG_TAG, "send intent: Restart HwService");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "clientConnect close: " + e);
        }
    }

    private void close() {
        try {
            clientConnect.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Close connection error");
        }
        actions.writeToFile("false", connection);
    }

    private boolean getStatus(){
        boolean statusConnection = Boolean.valueOf(actions.readFromFile(connection));
        return statusConnection;
    }
}
