package com.dev.toxa.integrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class ClientConnect {
    final String LOG_TAG = "ClientConnect";
    private Socket socket;
    private InetAddress ip;
    int port;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private boolean connection = false;
    private String msg = null;
    public boolean closeConnection = false;
    Thread thread = new Thread();



    public void connect(String ip, int port) {
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Open Connection Error");
        }
        try {
            start_input_stream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        start_output_stream();
    }

    // Открытие соединения
    public boolean openConnection() throws IOException{
        try {
            socket = new Socket(ip, port);
            connection = true;
            Log.d(LOG_TAG, "Подключено");
        } catch (IOException e) {
            Log.d(LOG_TAG, "openConnection error");
            e.printStackTrace();
            connection = false;
        }
        return connection;

    }

    //  Создание входящего потока
    public void start_input_stream() throws IOException {
        try {
            in = new DataInputStream(socket.getInputStream());  // Создание потока на прием
            Log.d(LOG_TAG, "Создан входящий поток");
        }
        catch (IOException e) {
            System.err.println("start_input_stream error");
            connection = false;
            e.printStackTrace();
        }
    }

    //  Создание исходящего потока
    public void start_output_stream() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            Log.d(LOG_TAG, "Создан исходящий поток");

        } catch (IOException e) {
            connection = false;
            e.printStackTrace();
        }
    }

    //  Чтение из потока
    public String receive() throws IOException {
        String str = null;
        try {
            Log.d(LOG_TAG, "Чтение из потока");
            str = in.readUTF ();
            Log.d(LOG_TAG, "Прочитано : " + str);
        }
        catch (IOException e) {
            System.err.println("receive error");
        }
        return str;
    }

    //  Запись в исходящий поток
    public void send(String msg) {
        try {
            Log.d(LOG_TAG, "Отправка : " + msg);
            if (out != null){
                out.writeUTF(msg);  // Отправка сообщения
                Log.d(LOG_TAG, "Отправлено");
                out.flush();    // Очистка буфера
            }
            else {}
        }
        catch (IOException e) {
            System.err.println("send error");
        }

    }

    public DataInputStream getInput() {
        return in;
    }
    //  Закрытие соединения
    public void close() throws IOException{
        try {
            out.close();
        }
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "out closed");
        }
        if (socket.isOutputShutdown()){
            Log.d(LOG_TAG, "Закрыт исходящий поток");
        }
        try {
            in.close();
        }
        catch (NullPointerException e) {
            Log.d(LOG_TAG, "in closed");
        }
        if (socket.isInputShutdown()){
            Log.d(LOG_TAG, "Закрыт входящий поток");
        }
        socket.close();
        if (socket.isClosed()){
            Log.d(LOG_TAG, "Соединение разоравано");
        }
    }
    public boolean connectionState() {
        return connection;
    }


}
