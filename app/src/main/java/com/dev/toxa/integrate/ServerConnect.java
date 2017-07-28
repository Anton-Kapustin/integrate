package com.dev.toxa.integrate;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ServerConnect {
    String LOG_TAG = "ServerConnect";
    private int port;
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    public boolean connection = false;
    String message = null;
    String cmd = null;


    public ServerConnect(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    //  Старт сервера на порту
    public void connect() throws IOException {
        try {
            clientSocket = serverSocket.accept();   //  Подключение клиента
        } catch (SocketException e) {
            System.err.println("Connect error");
            connection = false;
        }
        connection = true;
        start_output_stream();
        start_input_stream();
    }

    // Прием сообщения
    public void start_input_stream() throws IOException {
        try {
            in = new DataInputStream(clientSocket.getInputStream()); // Создание потока данных приема
        } catch (SocketException e) {
            connection = false;
            System.err.println(e);
            in.close();
        }
    }

    public String receive() throws IOException {
        try {
            Log.d(LOG_TAG,"Получение");
//            if(!clientSocket.isInputShutdown()) {
                cmd = in.readUTF();
                Log.d(LOG_TAG,"Получено");
                if(cmd != null) {
                    Log.d(LOG_TAG,"Получено: " + cmd);
                } else {
                    Log.d(LOG_TAG,"Null");
                }
//            } else {
//                in.close();
//                clientSocket.close();
//            }
        }
        catch(EOFException e) {
            Log.d(LOG_TAG,"receive error");
            this.clientSocket.close();
            return null;
        }
        return cmd;

    }

    // Отправка сообщения
    public void start_output_stream() throws IOException {

        try {
            out = new DataOutputStream(clientSocket.getOutputStream()); //  Создание потока данных отправки
        } catch (IOException e) {
            connection = false;
            System.err.println(e);
        }

    }

    public void send(String message) throws IOException {
        try {
            out.writeUTF(message);  //  Отпрака сообщения
            Log.d(LOG_TAG, "Отправка");
            out.flush();    //  Очистка буфера
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "send error");
        }
    }

    public DataInputStream getInput() {
        return in;
    }

    public void close() throws IOException {
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }


    public boolean get_state() {
        return connection;
    }
}