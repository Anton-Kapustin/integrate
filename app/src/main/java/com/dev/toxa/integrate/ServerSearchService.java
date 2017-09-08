package com.dev.toxa.integrate;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.*;

public class ServerSearchService extends IntentService {
    String LOG_TAG = "ServerSearchService: ";
    String actionToFragmentListServers = "com.dev.toxa.integrate.FragmentListServers";

    public ServerSearchService() {
        super("ServerSearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        receiveBroadcast();
    }

    public void receiveBroadcast() {
        DatagramSocket socketBroadcast = null;
        try {
            socketBroadcast = new DatagramSocket(18032);
            Log.d(LOG_TAG,"Создан сервер udp");
            byte[] buffer = new byte[256];
            DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
            Log.d(LOG_TAG,"Создан пакет udp");
            socketBroadcast.setSoTimeout(5000);
            socketBroadcast.receive(udpPacket);
            Log.d(LOG_TAG,"Получен пакет udp");
            String broadcastMessage = new String(buffer, 0, udpPacket.getLength());
            if (broadcastMessage.contains("integrate")) {
                InetAddress addr = udpPacket.getAddress();
                String address = addr.getHostAddress();
                Log.d(LOG_TAG, "Broadcast получен: " + broadcastMessage + " от: " + address);
                String [] array = broadcastMessage.split(", ");
                Log.d(LOG_TAG, array.toString());
                for (int i = 0; i < array.length; i++){
                    if (array[i].contains("name")) {
                        Log.d(LOG_TAG, array[i].toString());
                        String name = array[i].replaceAll("name: ", "");
                        Log.d(LOG_TAG, "name: " + name);
                        Intent intentToMainService = new Intent(actionToFragmentListServers);
                        intentToMainService.putExtra("parameters", "foundServer");
                        intentToMainService.putExtra("serverAddress", address);
                        intentToMainService.putExtra("serverName", name);
                        sendBroadcast(intentToMainService);
                    }
                }
            }
        } catch (SocketException e) {
            Log.d(LOG_TAG, "socketBroadcast error: " + e);
            e.printStackTrace();
            socketBroadcast.close();
            receiveBroadcast();
        } catch (SocketTimeoutException e) {
            Log.e(LOG_TAG, "timeout: " + e);
            socketBroadcast.close();
            receiveBroadcast();
        } catch (IOException e) {
            Log.d(LOG_TAG, "I/O error: " + e);
            e.printStackTrace();
            socketBroadcast.close();
            receiveBroadcast();
        }
        socketBroadcast.close();
        receiveBroadcast();
    }
}
