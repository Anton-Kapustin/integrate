package com.dev.toxa.integrate;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class StatusPhoneHwService extends IntentService {
    final String LOG_TAG = "statusPhoneHwService";
    String actionUpdateData = "com.dev.toxa.integrate.StatusPhoneHwService.UpdateData";
    Intent sendInfoIntent = new Intent("com.dev.toxa.integrate.StatusHwPhoneState.Info");
    Intent intentUpdateData = new Intent(actionUpdateData);
    ServerConnect server = null;
    ActionsWithFile actions = new ActionsWithFile();
    String batteryFile = "battery";
    String networkFile = "network";
    String state = null;
    String battery = null;
    String network = null;


    public StatusPhoneHwService() {
        super("statusPhoneHwService");
    }

    public static void startAction(Context context) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String[] array = new String[]{batteryFile, networkFile};
        boolean connection = false;
        Map cmd;
        int port = 18031;
        int count = 0;
        while (true) {
        try {
            Log.d(LOG_TAG, "Запуск сервера");
            server = new ServerConnect(port);
            server.connect();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Server Connect error");
        }

            Log.d(LOG_TAG, "Получение комманд");
            JSONObject jsonReceive = null;
            try {
                jsonReceive = server.receive();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (jsonReceive.has("phone")) {
                sendBroadcast(sendInfoIntent);
                if (jsonReceive.has("battery")) {
                    intentUpdateData.putExtra("parameters", jsonReceive.toString());
                    sendBroadcast(intentUpdateData);
                    actions.writeToFile(jsonReceive.toString(), batteryFile);
                }
            }


//            Set<Map.Entry> set = cmd.entrySet();
//            for (Map.Entry entry : set) {
//                if (Arrays.asList(array).contains(entry.getKey())) {
//                    actions.writeToFile(entry.getValue(), entry.getKey());
//                } else if (entry.getKey().equals("phone")) {
//                    sendBroadcast(sendInfoIntent);
//                    Log.d(LOG_TAG, "Broadcast to Command service");
//                }
//            }
            //                        Log.d(LOG_TAG, "Получено: " + cmd);
            //                    if (cmd.contains("end")){
            //                        break;
            //                    } else if (cmd.contains("phone")) {
            //                        sendBroadcast(sendInfoIntent);
            //                        Log.d(LOG_TAG, "Broadcast to Command service");
            //
            //                        Log.d(LOG_TAG, "Broadcast to Command Service receive");
            //                    } else if (cmd.contains("battery")) {
            //                        Log.d(LOG_TAG, cmd);
            //                        str[0] = cmd;
            //                    } else if (cmd.contentEquals("network")) {
            //                        Log.d(LOG_TAG, cmd);
            //                        str[0] = cmd;
            //                    } else {
            //                        str[1] = cmd;
            //                        if(str[0].contains("battery")) {
            //                            actions.writeToFile(str[1], batteryFile);
            //                        } else if (str[0].contains("network")) {
            //                            actions.writeToFile(str[1], networkFile);
            //                        }
            //                    }
            //                } catch (IOException e) {
            //                    Log.d(LOG_TAG, "server receive error");
            //                    e.printStackTrace();
            //                }

            try {
                server.close();
            } catch (IOException e) {
                Log.d(LOG_TAG, "server close error");
                e.printStackTrace();
            }
            Log.d(LOG_TAG, "Соединение разорвано");
        }
    }
}

