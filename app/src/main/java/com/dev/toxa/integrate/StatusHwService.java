package com.dev.toxa.integrate;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import java.io.IOException;

public class StatusHwService extends IntentService{
    final String LOG_TAG = "StatusHwService";
    final int port = 8030;
    final String ip = "192.168.88.73";
    String batteryFile = "battery";
    String networkFile = "network";
    String state = null;
    String connection = "connection";
    String status = "status";
    Intent intentToMain = new Intent("com.dev.toxa.integrate.ClientConnect.Broadcast");
//    Intent intentToService = new Intent(Intent.ACTION_TIME_TICK);

    final ActionsWithFile actions = new ActionsWithFile();

    public StatusHwService(){
        super("StatusHwService");
    }

    public static void startAction(Context context) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ClientConnect clientConnect = new ClientConnect();
        clientConnect.connect(ip, port);
        clientConnect.send("info");

//        try {
//            state = clientConnect.receive();
//            Log.d(LOG_TAG, "Получено :" + state);
//            Log.d(LOG_TAG, "Пишем в файл :" + state);
//            actions.writeInFile(state, "battery");
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "recieve error");
//            e.printStackTrace();
//        }
//        clientConnect.send("network");
//        try {
//            state = clientConnect.receive();
//            actions.writeInFile(state, "network");
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "recieve error");
//            e.printStackTrace();
//        }
        clientConnect.send("end");
        try {
            clientConnect.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Close connection error");
        }
        actions.writeInFile("false", connection);
//        sendBroadcast(intentToMain);
//        Log.d(LOG_TAG, "Broadcasts send");
    }
    private boolean getStatus(){
        boolean statusConnection = Boolean.valueOf(actions.readFromFile(connection));
        return statusConnection;
    }
}
