package com.dev.toxa.integrate;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.lang.reflect.Array;

public class StatusHwService extends IntentService{
    final String LOG_TAG = "statusHwService";
    final int port = 18030;
    final String ip = "192.168.88.73";
    String batteryFile = "battery";
    String networkFile = "network";
    String backlight = "backlight";
    String phoneInfo = "phoneInfo";
    String parameters = "parameters";
    String state = null;
    String connection = "connection";
    String status = "status";
    String actionButton = "com.dev.toxa.integrate.ClientConnect.actionButton";
    String actionRestartService = "com.dev.toxa.integrate.ClientConnect.actionRestartService";
    Intent intentToMainButton = new Intent(actionButton);
    Intent intentToMainService = new Intent(actionRestartService);
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
        String action = intent.getAction();
        String params = intent.getStringExtra("parameters");
        if (params.contains("firstRun")) {
            intentToMainButton.putExtra("parameters", "Button_connect_disable");
            sendBroadcast(intentToMainButton);
            Log.d(LOG_TAG, "send intent: Button_connect_disable");
        }
        Log.d(LOG_TAG, "Запуск клиента");
        clientConnect = new ClientConnect();
        clientConnect.connect(ip, port);
        if (clientConnect.getConnectionState()) {
//            if (parameters.contains("firstRun")) {
            intentToMainButton.putExtra("parameters", "Button_connect_close");
            sendBroadcast(intentToMainButton);
            Log.d(LOG_TAG, "send intent: Button_connetct_close");
//            }
            if (intent.getAction() != null) {
                Log.d(LOG_TAG, "intent.getAction(): " + intent.getAction());
                if ((intent.getAction()).contains("Info.Send")) {
                    Log.d(LOG_TAG, "Содержит");
//                    Bundle bundle = new Bundle();
//                    bundle = intent.getExtras();
                    Log.d(LOG_TAG, intent.getAction());
                    Log.d(LOG_TAG, params);
                    try {
                        Log.d(LOG_TAG, params);
                        if (params.contains("light")) {
                            Log.d(LOG_TAG, "2");
                            int i = 0;
                            int back = intent.getIntExtra("light", i);
                            String backlightState = backlight + " " + String.valueOf(back);
                            clientConnect.send(backlightState);
                            clientConnect.send("end");
                        }
                    } catch (NullPointerException e) {
                        Log.d(LOG_TAG, "Нет параметра " + backlight);
                    }
                    try {
                        if (params.contains("phoneInfo")) {
                            String status = intent.getStringExtra("bat");
//                            Log.d(LOG_TAG, "Отправка: " + status);
//                            clientConnect.send(status);
//                            clientConnect.send("end");
//                            Log.d(LOG_TAG, "Отправлено");
                            status += "&";
                            status += intent.getStringExtra("net");
                            Log.d(LOG_TAG, "Отправка: " + status.toString());
                            clientConnect.send(status);
                            clientConnect.send("end");
                            Log.d(LOG_TAG, "Отправлено");
//                            Log.d(LOG_TAG, intent.getStringExtra("net"));
                            close();
                        }
                    } catch (NullPointerException e) {
                        Log.d(LOG_TAG, "Нет параметра " + phoneInfo);
                    }
                    try {
                        if (params.contains("sound")) {
                            int i = 0;
                            String soundState = String.valueOf(intent.getIntExtra("snd", i));
                            String status = "sound: " + soundState;
                            Log.d(LOG_TAG, "Отправка: " + status);
                            clientConnect.send(status);
                            clientConnect.send("end");
                            Log.d(LOG_TAG, "Отправлено");
                            close();
                        }
                    } catch (NullPointerException e) {
                        Log.d(LOG_TAG, "Нет параметра " + "sound");
                    }
                    try {
                        if (params.contains("notification")) {
                            String notification = intent.getStringExtra("notify");
                            String notify = "notify/ " + notification;
                            clientConnect.send(notify);
                            clientConnect.send("end");
                        }
                    } catch (NullPointerException e) {
                        Log.d(LOG_TAG, "Нет параметра " + "sound");
                    }
                }
            } else {
                Log.d(LOG_TAG, "Не содержит");
                clientConnect.send("info");
                clientConnect.send("end");
                Log.d(LOG_TAG, "Сообщение отправлено");
                close();
            }

        } else {
            intentToMainService.putExtra("parameters", "firstRun");
            sendBroadcast(intentToMainService);
            Log.d(LOG_TAG, "send intent: Restart HwService");
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
