package com.dev.toxa.integrate.Network;

import android.util.Log;
import com.dev.toxa.integrate.FragmentConnetctToServer.PresenterFragmentConnectToServer;
import com.dev.toxa.integrate.LoggingNameClass;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;


public class ClientConnect {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private PresenterFragmentConnectToServer presenter = null;
    private Socket clientSocket = null;
    private InetAddress ip;
    private int port = 18030;
    InputStream in = null;
    OutputStream out = null;
    private boolean connection = false;
    private int errorCount = 0;

    public ClientConnect(PresenterFragmentConnectToServer presenter) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        this.presenter = presenter;
    }


    public boolean connect(String ip, int port) {
        if (ip != null) {
            try {
                this.ip = InetAddress.getByName(ip);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            this.port = port;
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            int timeout = 5000;
            try {
                if (openConnection(socketAddress, timeout)) {
                    try {
                        start_input_stream();
                        start_output_stream();
                        connection = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        connection = false;
                    }
                } else {
                    connection = false;
                }
            } catch (IOException e) {
                connection = false;
                e.printStackTrace();
                Log.e(LOG_TAG, "Open Connection Error");
            }
        }
        return connection;
    }

    public void sendToServer(final String IP, final String data) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        SocketAddress socketAddress = null;
        Log.d(LOG_TAG, "IP: " + IP);
        Log.d(LOG_TAG, "data: " + data);
        if (IP != null) {
            try {
                ip = InetAddress.getByName(IP);
                socketAddress = new InetSocketAddress(ip, port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            int timeout = 5000;
            try {
                if (openConnection(socketAddress, timeout)) {
                    start_output_stream();
                    send(data);
                    Log.d(LOG_TAG, "Отправлено: " + data);
                    close();
                    errorCount = 0;
                    presenter.startServer();
                } else {
                    errorCount ++;
                    if (errorCount <= 3) {
                        sendToServer(IP, data);
                    } else {
                        presenter.stopServer();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Open Connection Error");
                errorCount ++;
                if (errorCount <= 3) {
                    sendToServer(IP, data);
                } else {
                    presenter.stopServer();
                }
            }
        }
    }


    // Открытие соединения
    public boolean openConnection(SocketAddress socketAddr, int timeOut) throws IOException{
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        try {
//            clientSocket = new Socket(ip, port);
            clientSocket = new Socket();
            clientSocket.connect(socketAddr, timeOut);
            connection = true;
            Log.d(LOG_TAG, "Подключено");
        } catch (NoRouteToHostException | ConnectException e) {
            Log.d(LOG_TAG, "openConnection error: " + e);
            e.printStackTrace();
            connection = false;
        } catch (SocketTimeoutException e) {
            Log.d(LOG_TAG, "openConnection error: " + e);
            e.printStackTrace();
            connection = false;
        } catch (IOException e) {
            Log.d(LOG_TAG, "openConnection error: " + e);
            e.printStackTrace();
            connection = false;
        }
        Log.d(LOG_TAG, "Открытие соединения: " + connection);
        return connection;

    }

    //  Создание входящего потока
    public void start_input_stream() throws IOException {
        try {
            in = clientSocket.getInputStream();
//            in = new DataInputStream(clientSocket.getInputStream());  // Создание потока на прием
            Log.d(LOG_TAG, "Создан входящий поток");
        }
        catch (IOException e) {
            Log.e(LOG_TAG,"start_input_stream error: " + e);
            connection = false;
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e(LOG_TAG,"start_input_stream error: " + e);
        }
    }

    //  Создание исходящего потока
    public void start_output_stream() {
        try {
            out = clientSocket.getOutputStream();
//            out = new DataOutputStream(clientSocket.getOutputStream());
            Log.d(LOG_TAG, "Создан исходящий поток");

        } catch (IOException e) {
            connection = false;
            e.printStackTrace();
        }
    }

    //  Чтение из потока
    public String receive() throws IOException {
        String str = null;
        Log.d(LOG_TAG, "Чтение из потока");
//            str = in.readUTF ();
        Log.d(LOG_TAG, "Прочитано : " + str);
        return str;
    }

    //  Запись в исходящий поток
    public void send(String msg) {
        msg = msg.replace("\n", "").replace("\r", "");
        Log.d(LOG_TAG, "msg: " + msg);
        try {
            Log.d(LOG_TAG, "Отправка : " + msg);
            if (out != null){
                byte[] b = msg.getBytes(Charset.forName("UTF-8"));
                out.write(b);  // Отправка сообщения
                Log.d(LOG_TAG, "Отправлено");
                out.flush();    // Очистка буфера
            }
        } catch (IOException e) {
            System.err.println("send error");
        }

    }


//    public DataInputStream getInput() {
//        return in;
//    }
    //  Закрытие соединения
    public void close() throws IOException{
        try {
            out.close();
        }
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "out closed");
        }
        try {
            if (clientSocket.isOutputShutdown()) {
                Log.d(LOG_TAG, "Закрыт исходящий поток");
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "clientServer is null");
        }
        try {
            in.close();
        }
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "in closed");
        }
        try {
            if (clientSocket.isInputShutdown()) {
                Log.d(LOG_TAG, "Закрыт входящий поток");
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "clientServer is null");
        }
        try {
            clientSocket.close();
            if (clientSocket.isClosed()){
                Log.d(LOG_TAG, "Соединение разоравано");
            }
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "clientSocket is null");
        }

    }
    public boolean getConnectionState() {
        return connection;
    }


}
