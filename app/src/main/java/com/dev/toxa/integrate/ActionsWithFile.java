package com.dev.toxa.integrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.*;

import static android.os.Environment.getExternalStorageState;

/**
 * Created by toxa on 12.07.17.
 */
public class ActionsWithFile {
    String LOG_TAG = "ActionsWithFiles";
    String batteryFilename = "battery.txt";
    String statusFilename = "network.txt";
    String networkFilename = "status.txt";
    String connectionFilename = "connection.txt";
    File sdcard = Environment.getExternalStorageDirectory();
    File batteryFile = new File(sdcard, batteryFilename);
    File networkFile = new File(sdcard, networkFilename);
    File statusFile = new File(sdcard, statusFilename);
    File connectionFile = new File(sdcard, connectionFilename);


    public String readFromFile(String filename) {
        String state = null;
        File file = null;
        if (filename.contains("battery")) {
            file = batteryFile;
        } else if (filename.contains("network")) {
            file = networkFile;
        } else if (filename.contains("status")) {
            file = statusFile;
        } else if (filename.contains("connection")) {
            file = connectionFile;
        }
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            state = bufferedReader.readLine();
            Log.d(LOG_TAG, "Прочитано : " + state);
        } catch (IOException e){
            Log.d(LOG_TAG, "read from file error");
            e.printStackTrace();
        }
        return state;
    }
    public void writeToFile(String state, String filename) {
        File file = null;
        if (filename.contains("battery")){
            file = batteryFile;
        } else if (filename.contains("network")) {
            file = networkFile;
        } else if (filename.contains("status")) {
            file = statusFile;
        } else if (filename.contains("connection")) {
            file = connectionFile;
        }
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(state);
            bufferedWriter.close();
            Log.d(LOG_TAG, "Записано : " + state + " в : " + file);
        } catch (IOException e){
            Log.d(LOG_TAG, "write to file error");
            e.printStackTrace();
        }
    }

    public void writeToFile(Object state, Object filename) {
        File file = null;
        String status = (String) state;
        String flname = (String) filename;
        if (flname.contains("battery")){
            file = batteryFile;
        } else if (flname.contains("network")) {
            file = networkFile;
        } else if (flname.contains("status")) {
            file = statusFile;
        } else if (flname.contains("connection")) {
            file = connectionFile;
        }
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(status);
            bufferedWriter.close();
            Log.d(LOG_TAG, "Записано : " + state + " в : " + file);
        } catch (IOException e){
            Log.d(LOG_TAG, "write to file error");
            e.printStackTrace();
        }
    }


}
