package com.dev.toxa.integrate.FragmentConnetctToServer;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.Network.ClientConnect;
import com.dev.toxa.integrate.Network.ServerConnect;
import org.json.JSONObject;
import java.io.IOException;

public class ConnectToServer {

    //===========================================Переменные=============================================================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private ClientConnect clientConnect;
    private int port = 18030;
    PresenterFragmentConnectToServer presenterFragmentConnectToServer;
    int connectionCount = 0;
    ServerConnect server = null;
    boolean connectionState = false;
    //==================================================================================================================

    public ConnectToServer(PresenterFragmentConnectToServer presenter) {
        presenterFragmentConnectToServer = presenter;
    }

    public void sendMessage(final String IP, final String message) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "IP: " + IP);
                    clientConnect = new ClientConnect();
                    if (sendMessageToServer(IP, message)) {
                    presenterFragmentConnectToServer.startServer();
                        try {
                            clientConnect.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        connectionCount = 0;
                    } else {
                        connectionCount ++;
                        Log.d(LOG_TAG, "count: " + connectionCount);
                    }
                    if (connectionCount == 1) {
                        presenterFragmentConnectToServer.stopServer();
                        connectionCount = 0;
                    }
            }
        });
        thread.start();
    }

    private boolean sendMessageToServer (String IP, String message) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (clientConnect.connect(IP, port)) {
            clientConnect.send(message);
            return true;
        } else {
            return false;
        }
    }

    public void runServer(final String IP) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (presenterFragmentConnectToServer.getStatusConnection()) {
                    startServerForReceive(IP);
                }
            }
        });
        thread.start();
    }

    private void startServerForReceive(String IP) {
        int port = 18031;
        int count = 0;
        try {
            Log.d(LOG_TAG, "Запуск сервера connectionState: " + connectionState);
            server = new ServerConnect(port);
            server.connect();
            connectionState = true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Server Connect error");
        }
        Log.d(LOG_TAG, "Получение комманд");
        JSONObject jsonReceive = null;
        try {
            jsonReceive = server.receive();
            if (jsonReceive.has("phone")) {
                presenterFragmentConnectToServer.threadToUI();
            }
            if (jsonReceive.has("PC_info")) {
                Log.d(LOG_TAG, "Получено: " + jsonReceive.toString());
                presenterFragmentConnectToServer.updateData(jsonReceive);
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

    public boolean getConnectionState() {
        return connectionState;
    }
}
