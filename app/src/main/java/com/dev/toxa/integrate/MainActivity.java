package com.dev.toxa.integrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

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
    TextView text_backlight;
    TextView text_sound;
    SeekBar seekBar_backlight;
    SeekBar seekBar_sound;
    Button button_connect;
    ImageView imageView_battery;
    String actionUpdateData = "com.dev.toxa.integrate.StatusPhoneHwService.UpdateData";
    String actionButton = "com.dev.toxa.integrate.ClientConnect.actionButton";
    String actionToCommnandServiceBacklight = "com.dev.toxa.integrate.CommnadService.backlight";
    String actionToCommnandServiceSound = "com.dev.toxa.integrate.CommnadService.sound";
    String actionNotify = "com.dev.toxa.integrate.NotifyListenerService.Notify";

    TextView textView;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        receiver = new MainBroadcastReceiver();
        registerReceiver(receiver, (new IntentFilter(actionUpdateData)));
        registerReceiver(receiver, (new IntentFilter(actionButton)));
        registerReceiver(receiver, (new IntentFilter(actionNotify)));
        setContentView(R.layout.activity_main);
        connectionStatus = (TextView) findViewById(R.id.statusConnection);
        batteryStatus = (TextView) findViewById(R.id.batteryStatus);
        networkStatus = (TextView) findViewById(R.id.networkStatus);
        text_backlight = (TextView) findViewById(R.id.text_backlight);
        text_sound = (TextView) findViewById(R.id.text_sound);
        button_connect = (Button) findViewById(R.id.button_connect);
        button_refrash = (Button) findViewById(R.id.button_refrash);
        imageView_battery = (ImageView) findViewById(R.id.imageView_battery);
        seekBar_backlight = (SeekBar) findViewById(R.id.seekBar_backlight);
        seekBar_sound = (SeekBar) findViewById(R.id.seekBar_sound);
        context = getApplicationContext();
        actions.writeToFile("no data", connection);
        actions.writeToFile("no data", battery);
        actions.writeToFile("no data", network);
        button_refrash.setEnabled(false);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);

        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (countButtonPressed == 0) {
                    actions.writeToFile("true", connection);
                    MainService = new Intent(MainActivity.this, MainService.class);
                    startService(MainService);
                    countButtonPressed++;
//                    button_connect.setText("Close");
                    button_refrash.setEnabled(true);
                    boolean statusConnection = Boolean.valueOf(actions.readFromFile(connection));
                    if (statusConnection) {
                        connectionStatus.setText("Connected");
                    } else {
                        connectionStatus.setText("No connection");
                    }
                    Log.i(LOG_TAG, "Button pressed connect (" + countButtonPressed + ")");
//                    refrash();
                } else {
                    countButtonPressed--;
                    stopService(MainService);
                    Log.i(LOG_TAG, "Button pressed close (" + countButtonPressed + ")");
//                    sendBroadcast(new Intent(context, SocketThread.class));
                    button_connect.setText("Connect");
                    button_refrash.setEnabled(false);
                    connectionStatus.setText("disconnected");
                    actions.writeToFile("false", status);


                }
            }
        });

        Log.d(LOG_TAG, " before battery button");

        button_refrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startService(MainService);
                String state = null;
                state = actions.readFromFile("battery");
                Log.d(LOG_TAG, state);
                try {
                    JSONObject jsonObject = new JSONObject(state);
                    updateData(jsonObject);
                } catch (JSONException e) {
                    Log.d(LOG_TAG, "Json error" + e);
                    e.printStackTrace();
                }

//                batteryStatus.setText(state);
//                state = actions.readFromFile("network");
//                Log.d(LOG_TAG, state);
//                networkStatus.setText(state);
            }
        });

        seekBar_backlight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text_backlight.setText(String.valueOf(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                Log.d(LOG_TAG, "SeekBar : " + i);
                Intent intentToCommandService = new Intent(actionToCommnandServiceBacklight);
                intentToCommandService.putExtra("backlight", i);
                sendBroadcast(intentToCommandService);
            }
        });

        seekBar_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text_sound.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                Log.d(LOG_TAG, "SeekBar : " + i);
                Intent intentToCommandService = new Intent(actionToCommnandServiceSound);
                intentToCommandService.putExtra("sound", i);
                sendBroadcast(intentToCommandService);
            }
        });


    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d(LOG_TAG, "POWER BUTTON");
        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
            connectionStatus.setText("POWER!!!");
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, (new IntentFilter(actionUpdateData)));
        String state = actions.readFromFile(battery);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(state);
            updateData(jsonObject);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Json error" + e);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Json error" + e);
        }

        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public void updateData(JSONObject jsonObject) {
        try {
            String bat = jsonObject.getString("battery");
            int charge = Integer.parseInt(bat.replaceAll("[^\\d.]", ""));
            if (0 < charge && charge <= 20) {
                if (bat.contains("Charg")){
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_20_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_20_black_24dp);
                }
            } else if (20 < charge  && charge <= 30) {
                if (bat.contains("Charg")){
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_30_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_30_black_24dp);
                }
            } else if (30 < charge  && charge <= 50) {
                if (bat.contains("Charg")){
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_50_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_50_black_24dp);
                }
            } else if (50 < charge  && charge <= 60) {
                if (bat.contains("Charg")){
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_60_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_60_black_24dp);
                }
            } else if (60 < charge  && charge <= 80) {
                if (bat.contains("Charg")){
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_80_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_80_black_24dp);
                }
            } else if (80 < charge  && charge <= 90) {
                if (bat.contains("Charg")){
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_90_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_90_black_24dp);
                }
            } else if (90 < charge  && charge <= 100) {
                if (bat.contains("Charg")){
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_full_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_full_black_24dp);
                }
            }
            batteryStatus.setText(charge + "%");
            String net = jsonObject.getString("network");
            String [] str = net.split(" ");
            for (int i = 0; i < str.length; i ++) {
                if (str[i].contains("wifi")) {
                    String network = str[i+1];
                }
            }
            networkStatus.setText("SSID: " + network);
            String back = jsonObject.getString("backlight");
            Double val = Double.valueOf(back);
            int backlight = Integer.valueOf(val.intValue());
            text_backlight.setText(String.valueOf(backlight));
            seekBar_backlight.setProgress(backlight);
            String snd = jsonObject.getString("sound").replaceAll("\\D+", "");
            int soundVol = Integer.parseInt(snd);
            seekBar_sound.setProgress(soundVol);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Json error: " + e);
            e.printStackTrace();
        }
    }

    public void changeButton(String parameter) {
        if (parameter.contains("disable")) {
            button_connect.setEnabled(false);
        } else if (parameter.contains("close")) {
            button_connect.setEnabled(true);
            button_connect.setText("Close");
        }
    }

    public class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            context = context.getApplicationContext();
            String action = intent.getAction();
            String parameters = intent.getStringExtra("parameters");
            if (action.contains("actionButton")) {
                if (parameters.contains("Button_connect_disable")) {
                    changeButton("disable");
                } else if (parameters.contains("close")) {
                    changeButton("close");
                }
            } else if (action.contains("UpdateData")) {
//                context.registerReceiver(receiver, (new IntentFilter(actionUpdateData)));
                JSONObject jsonData = null;
                try {
                    jsonData = new JSONObject(intent.getStringExtra("parameters"));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Json error: " + e);
                    e.printStackTrace();
                }

                Log.d(LOG_TAG, "Broadcast in Main ACtivity");
                updateData(jsonData);
            } else if (action.contains("Notify")) {
                textView.setText(intent.getStringExtra("title"));
                textView2.setText(intent.getStringExtra("text"));
            }
        }
    }
}