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
                try {
                    clientConnect = new ClientConnect();
                    clientConnect.connect(IP, port);
                    clientConnect.send(message);
                    presenterFragmentConnectToServer.startServer();
                    clientConnect.close();
                } catch (IOException e) {
                    connectionCount ++;
                    Log.d(LOG_TAG, "count: " + connectionCount);
                    if (connectionCount == 4) {
                        presenterFragmentConnectToServer.stopServer();
                    }
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void runServer(String IP) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int port = 18031;
                int count = 0;
                connectionState = true;
                while (connectionState) {
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
                            presenterFragmentConnectToServer.sendPhoneInfo();
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
            }
        });
        thread.start();
    }

    public void setConnetionState(boolean state) {
        connectionState = state;
    }

    public boolean getConnectionState() {
        return connectionState;
    }

    interface CallbackToPresenter {
        void startServer();
        void stopServer();
        void sendPhoneInfo();
        void updateData(JSONObject jsonData);
    }
}
