package com.dev.toxa.integrate.Network;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ServerConnect {
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private int port;
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    InputStream inString = null;
    OutputStream outString =null;
    public boolean connection = false;
    String message = null;
    String cmd = null;


    public ServerConnect(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        Log.d(LOG_TAG, "New Server");
    }

    //  Старт сервера на порту
    public void connect() {
        try {
            Log.d(LOG_TAG,"Ожидание подключения");
            socket = serverSocket.accept();   //  Подключение клиента
            Log.d(LOG_TAG,"Подключено");
        } catch (SocketException e) {
            Log.e(LOG_TAG,"Connect error" + e);
            connection = false;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Connect error" + e);
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "Открытие потоков");
        connection = true;
        start_output_stream();
        try {
            start_input_stream();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Start input stream error" + e);
            e.printStackTrace();
        }
    }

    private void start_input_stream() throws IOException {
        try {
            inString = socket.getInputStream();
//            inString = new DataInputStream(socket.getInputStream());  // Создание потока на прием
        }
        catch (IOException e) {
            System.err.println(LOG_TAG + "start_input_stream error");
            connection = false;
            e.printStackTrace();
        }
//        try {
//            inObject = new ObjectInputStream(socket.getInputStream());
//            Log.d(LOG_TAG, "Создан входящий поток");
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Start input stream error" + e);
//        }
    }

    private void start_output_stream() {
        try {
            outString = socket.getOutputStream();
//            outString = new DataOutputStream(socket.getOutputStream());   // Созадание потока на отпраку

        } catch (IOException e) {
            System.err.println(LOG_TAG + "start_output_stream error");
            connection = false;
            e.printStackTrace();
        }
//        try {
//            outObject = new ObjectOutputStream(socket.getOutputStream());
//            Log.d(LOG_TAG, "Создан исходящий поток");
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "Start output stream error" + e);
//        }
    }

    public JSONObject receive() throws IOException {
        JSONObject jsonObject = null;
        try {
            Log.d(LOG_TAG,"Получение");
//            if(!socket.isInputShutdown()) {
//                cmd = inString.readUTF();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[16384];
            int count;
            while ((count = inString.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, count);
            }
            jsonObject = new JSONObject(byteArrayOutputStream.toString());

            Log.d(LOG_TAG, "Получено: " + jsonObject.toString());
        }
        catch(EOFException e) {
            Log.d(LOG_TAG, "receive error");
            this.socket.close();
            return null;
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Json error: " + e);
            e.printStackTrace();
        }

        return jsonObject;
    }

//    public Map<String, String> receive() {
//        Map msg = new HashMap<String, String>();
//        Log.d(LOG_TAG, "Получение");
//        Object obj =null;
//        try {
//            obj = inObject.readObject();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "receive error" + e);
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            Log.e(LOG_TAG, "receive error" + e);
//            e.printStackTrace();
//        }
//        msg = (Map) obj;
//        return msg;
//    }

    public void send(String message) throws IOException {
        try {
//            outString.writeUTF(message);  //  Отпрака сообщения
            Log.d(LOG_TAG, "Отправка");
            outString.flush();    //  Очистка буфера
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "send error");
        }
    }

//    public ObjectInputStream getInput() {
//        return inObject;
//    }

    public void close() throws IOException {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
//        inObject.close();
//        outObject.close();
        try {
            outString.close();
            inString.close();
            socket.close();
            serverSocket.close();
            Log.d(LOG_TAG, "server connect close");
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "closing streams error: " + e);
        }
    }


    public boolean get_state() {
        return connection;
    }
}