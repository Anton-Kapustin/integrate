package com.dev.toxa.integrate.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import com.dev.toxa.integrate.ActionsWithFile;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.Network.ServerConnect;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class StatusPhoneHwService extends IntentService {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    String actionToFragmentConnectToServer = "com.dev.toxa.integrate.FragmentConnectToServer";
    String actionToCommnadService = "com.dev.toxa.integrate.CommnadService";

    Intent intentToCommandService = new Intent(actionToCommnadService);
    Intent intentFragmentConnectToServer = new Intent(actionToFragmentConnectToServer);

    ServerConnect server = null;
    ActionsWithFile actions = new ActionsWithFile();

    String batteryFile = "battery";
    String networkFile = "network";
    String parameters = "parameters";
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
                if (jsonReceive.has("phone")) {
                    intentToCommandService.putExtra(parameters, "phone");
                    sendBroadcast(intentToCommandService);
                }
                if (jsonReceive.has("PC_info")) {
                    intentFragmentConnectToServer.putExtra("parameters", "updateData");
                    intentFragmentConnectToServer.putExtra("data", jsonReceive.toString());
                    Log.d(LOG_TAG, "Получено: " + jsonReceive.toString());
                    sendBroadcast(intentFragmentConnectToServer);
                    actions.writeToFile(jsonReceive.toString(), batteryFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.d(LOG_TAG, "null error: " + e);
            }
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

