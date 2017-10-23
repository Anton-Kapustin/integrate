package com.dev.toxa.integrate.Services;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.dev.toxa.integrate.FragmentListServers.FragmentListServers;
import com.dev.toxa.integrate.LoggingNameClass;

import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceFindServers extends Service {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    Callback activity;
    boolean connection = false;

    Timer timer = new Timer();

    public ServiceFindBinder binder = new ServiceFindBinder();

    public ServiceFindServers() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
//        searchingServers();
        return START_NOT_STICKY;
    }

    public void searchingServers() {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                receiveBroadcast();
            }
        });

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (activity != null) {
                    thread.start();
                }
            }
        };
        timer.schedule(timerTask, 3000, 6000);

    }

    public void setActivity (FragmentListServers activity) {
        this.activity = (Callback) activity;
        if (activity != null) {
            Log.d(LOG_TAG, "activity not null");
        } else {
            Log.d(LOG_TAG, "activity null");
        }
    }

    void receiveBroadcast() {
        connection = true;
        DatagramSocket socketBroadcast = null;
        try {
            String PCName = null;
            String distr = null;
            socketBroadcast = new DatagramSocket(18032);
            Log.d(LOG_TAG, "Создан сервер udp");
            byte[] buffer = new byte[256];
            DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
            Log.d(LOG_TAG, "Создан пакет udp");
            socketBroadcast.setSoTimeout(5000);
            socketBroadcast.receive(udpPacket);
            Log.d(LOG_TAG, "Получен пакет udp");
            String broadcastMessage = new String(buffer, 0, udpPacket.getLength());
            if (broadcastMessage.contains("integrate")) {
                InetAddress addr = udpPacket.getAddress();
                String address = addr.getHostAddress();

                Log.d(LOG_TAG, "Broadcast получен: " + broadcastMessage + " от: " + address);
                String[] array = broadcastMessage.split(", ");
                Log.d(LOG_TAG, array.toString());
                for (int i = 0; i < array.length; i++) {
                    if (array[i].contains("name")) {
                        PCName = array[i].replaceAll("name: ", "");
                        Log.d(LOG_TAG, "name: " + PCName);
                    } else if (array[i].contains("distr")) {
                        distr = array[i].replaceAll("distr: ", "");
                        Log.d(LOG_TAG, "distr: " + distr);
                    }
                }
                socketBroadcast.close();
                if ((address != null) && (PCName != null) && (distr != null)) {
                    if (activity != null) {
                        activity.foundServer(address, PCName, distr);
                        socketBroadcast.close();
                    } else {
                        Log.d(LOG_TAG, "Activity null");
                    }
                }
            }
        } catch (SocketException e) {
            Log.e(LOG_TAG, "socketBroadcast error: " + e);
            e.printStackTrace();

        } catch (SocketTimeoutException e) {
            Log.e(LOG_TAG, "timeout: " + e);

        } catch (IOException e) {
            Log.e(LOG_TAG, "I/O error: " + e);
            e.printStackTrace();
        }
        socketBroadcast.close();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "SeriveFindServers Bind");
//        searchingServers();
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean getConnection() {
        return connection;
    }

    public interface Callback {
        void foundServer(String IP, String serverName, String distro);
    }

    public class ServiceFindBinder extends Binder {
        public ServiceFindServers getService() {
            return ServiceFindServers.this;
        }
    }
}
