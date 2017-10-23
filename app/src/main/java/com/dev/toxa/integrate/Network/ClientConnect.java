package com.dev.toxa.integrate.Network;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;


public class ClientConnect {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private Socket clientSocket = new Socket();
    private InetAddress ip;
    int port;
//    private DataInputStream in = null;
//    private DataOutputStream out = null;
    InputStream in = null;
    OutputStream out = null;
    private boolean connection = false;
    private String msg = null;
    public boolean closeConnection = false;
    Thread thread = new Thread();



    public void connect(String ip, int port) {
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
    }


    // Открытие соединения
    public boolean openConnection(SocketAddress socketAddr, int timeOut) throws IOException{
        try {
//            clientSocket = new Socket(ip, port);
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
        if (clientSocket.isOutputShutdown()){
            Log.d(LOG_TAG, "Закрыт исходящий поток");
        }
        try {
            in.close();
        }
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "in closed");
        }
        if (clientSocket.isInputShutdown()){
            Log.d(LOG_TAG, "Закрыт входящий поток");
        }
        clientSocket.close();
        if (clientSocket.isClosed()){
            Log.d(LOG_TAG, "Соединение разоравано");
        }
    }
    public boolean getConnectionState() {
        return connection;
    }


}
