package com.dev.toxa.integrate.FragmentListServers;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;

import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class ServiceFindServers extends Service {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    Callback presenter;
    boolean connection = false;

    Timer timer = new Timer();

    Thread thread = null;

    public ServiceFindBinder binder = new ServiceFindBinder();

    //==================================================================================================================

    public int onStartCommand(Intent intent, int flags, int startId) {
//        searchingServers();
        return START_NOT_STICKY;
    }

    public void searchingServers() {



        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
                if (presenter != null) {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            receiveBroadcast();
                        }
                    });
                    thread.start();
                }
            }
        };
        timer.schedule(timerTask, 3000, 7000);
    }

    public void setPresenter(PresenterFragmentListServers presenter) {
        this.presenter = presenter;
        if (this.presenter != null) {
            Log.d(LOG_TAG, "presenter not null");
        } else {
            Log.d(LOG_TAG, "presenter null");
        }
    }

    void receiveBroadcast() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));

        connection = true;
        DatagramSocket socketBroadcast = null;
        try {
            String PCName = null;
            String macAddress = null;
            String distr = null;
            socketBroadcast = new DatagramSocket(18032);
//            Log.d(LOG_TAG, "Создан сервер udp");
            byte[] buffer = new byte[256];
            DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length);
//            Log.d(LOG_TAG, "Создан пакет udp");
            socketBroadcast.setSoTimeout(7000);
            socketBroadcast.receive(udpPacket);
//            Log.d(LOG_TAG, "Получен пакет udp");
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
//                        Log.d(LOG_TAG, "name: " + PCName);
                    } else if (array[i].contains("mac")) {
                        macAddress = array[i].replaceAll("mac: ", "").replaceAll("\\s+","");
//                        Log.d(LOG_TAG, "Mac address: " + macAddress);
                    } else if (array[i].contains("distr")) {
                        distr = array[i].replaceAll("distr: ", "");
//                        Log.d(LOG_TAG, "distr: " + distr);
                    }
                }
                socketBroadcast.close();
                if ((address != null) && (PCName != null) && (distr != null)) {
                    if (presenter != null) {
                        presenter.foundServer(address, PCName, macAddress, distr);
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
        if (socketBroadcast != null) {
            socketBroadcast.close();
        }
        Log.d(LOG_TAG, "broadcastReceiver close");
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "SeriveFindServers Bind");
//        searchingServers();
        return binder;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
//        timer.cancel();
//        thread.interrupt();
//        thread = null;
        super.onDestroy();
    }

    public void stopService() {
        timer.cancel();
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public boolean getConnection() {
        return connection;
    }

    public interface Callback {
        void foundServer(String IP, String serverName, String macAddress, String distro);
    }

    public class ServiceFindBinder extends Binder {
        public ServiceFindServers getService() {
            return ServiceFindServers.this;
        }
    }
}
