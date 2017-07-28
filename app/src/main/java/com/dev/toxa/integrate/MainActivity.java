package com.dev.toxa.integrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static Thread thread = null;
    private static final String LOG_TAG = "MyApp";
    ActionsWithFile actions = new ActionsWithFile();
    int countButtonPressed = 0;
    Context context;
    Intent MainService;
    String connection = "connection";
    String battery = "battery";
    String network = "network";
    String status = "status";
    MainBroadcastReceiver receiver;
    Button button_refrash;
    TextView connectionStatus;
    TextView batteryStatus;
    TextView networkStatus;
    Button button_connect;
    Button battery_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionStatus = (TextView) findViewById(R.id.statusConnection);
        batteryStatus = (TextView) findViewById(R.id.batteryStatus);
        networkStatus = (TextView) findViewById(R.id.networkStatus);
        button_connect = (Button) findViewById(R.id.button_connect);
        button_refrash = (Button) findViewById(R.id.button_refrash);
        battery_btn = (Button) findViewById(R.id.battery);
        context = getApplicationContext();
        actions.writeInFile("no data", connection);
        actions.writeInFile("no data", battery);
        actions.writeInFile("no data", network);
        button_refrash.setEnabled(false);
        battery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
                int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                connectionStatus.setText(String.valueOf(level));
            }
        });
        button_connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                receiver = new MainBroadcastReceiver();
                registerReceiver(receiver, (new IntentFilter("com.dev.toxa.integrate.ClientConnect.Broadcast")));
                if (countButtonPressed == 0){
                    actions.writeInFile("true", connection);
                    MainService = new Intent(MainActivity.this, MainService.class);
                    startService(MainService);
                    countButtonPressed++;
                    button_connect.setText("Close");
                    button_refrash.setEnabled(true);
                    boolean statusConnection = Boolean.valueOf(actions.readFromFile(connection));
                    if (statusConnection) {
                        connectionStatus.setText("Connected");
                    } else {
                        connectionStatus.setText("No connection");
                    }
                    Log.i(LOG_TAG, "Button pressed connect (" + countButtonPressed + ")");
                    refrash();
                } else {
                    countButtonPressed--;
                    stopService(MainService);
                    Log.i(LOG_TAG, "Button pressed close (" + countButtonPressed + ")");
//                    sendBroadcast(new Intent(context, SocketThread.class));
                    button_connect.setText("Connect");
                    button_refrash.setEnabled(false);
                    connectionStatus.setText("disconnected");
                    actions.writeInFile("false", status);


                }
            }
        });

        Log.d(LOG_TAG," before battery button");

        button_refrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                startService(MainService);
                String state = null;
                state = actions.readFromFile("battery");
                Log.d(LOG_TAG, state);
                batteryStatus.setText(state);
                state = actions.readFromFile("network");
                Log.d(LOG_TAG, state);
                networkStatus.setText(state);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    public void refrash() {
        String state = null;
        state = actions.readFromFile(battery);
        Log.d(LOG_TAG, state);
        batteryStatus.setText(state);
        state = actions.readFromFile(network);
        Log.d(LOG_TAG, state);
        networkStatus.setText(state);
    }
    public class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            context = context.getApplicationContext();
            context.registerReceiver(receiver, (new IntentFilter("com.dev.toxa.integrate.ClientConnect.Broadcast")));
            Log.i(LOG_TAG, "Broadcast in Main ACtivity");
            refrash();
        }
    }
}