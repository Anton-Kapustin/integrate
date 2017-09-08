package com.dev.toxa.integrate;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import layout.FragmentConnectToServer;
import layout.FragmentListServers;
import layout.FragmentSettings;


public class MainActivity extends AppCompatActivity {
    String LOG_TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        Intent mainService = new Intent(MainActivity.this, MainService.class);
        startService(mainService);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentListServers(), "Поиск серверов");
        adapter.addFragment(new FragmentConnectToServer(), "Управление ПК");
        adapter.addFragment(new FragmentSettings(), "Параметры");
        viewPager.setAdapter(adapter);
    }
    @Override
    protected void onPause() {
//        unregisterReceiver(receiver);
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(receiver);
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }
}


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.d(LOG_TAG, "POWER BUTTON");
//        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
//            connectionStatus.setText("POWER!!!");
//            return true;
//        }
//
//        return super.dispatchKeyEvent(event);
//    }


//        registerReceiver(receiver, (new IntentFilter(actionToFragmentConnectToServer)));
//        String state = actions.readFromFile(battery);
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(state);
//            updateData(jsonObject);
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "Json error" + e);
//        } catch (NullPointerException e) {
//            Log.e(LOG_TAG, "Json error" + e);
//        }


//    public void uiSearchServer(boolean isServerFind) {
//        if (!isServerFind) {
//            layout_connection.setVisibility(View.INVISIBLE);
//            layout_backlight.setVisibility(View.INVISIBLE);
//            layout_sound.setVisibility(View.INVISIBLE);
//            layout_searchServer.setVisibility(View.VISIBLE);
//            Log.d(LOG_TAG, "Интерфейс поиска");
//        } else {
//            layout_listServers.setVisibility(View.INVISIBLE);
//            layout_connection.setVisibility(View.VISIBLE);
//            layout_backlight.setVisibility(View.VISIBLE);
//            layout_sound.setVisibility(View.VISIBLE);
//            layout_searchServer.setVisibility(View.VISIBLE);
//            Log.d(LOG_TAG, "Интерфейс взаимодействия");
//        }
//    }

//    public void uiFindServers(Intent intentServers) {
//        final String serverAddress = intentServers.getStringExtra("serverAddress");
//        String serverName = intentServers.getStringExtra("serverName");
//        if (!serversIP.contains(serverAddress)){
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            Button buttonSrv = new Button(this);
//            buttonSrv.setLayoutParams(params);
//            int id = serversIP.size();
//            buttonSrv.setId(id);
//            buttonSrv.setText(serverName);
//            layout_listServers.addView(buttonSrv);
//
//            buttonSrv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    uiSearchServer(true);
//                    connectToServer(serverAddress);
//                }
//            });
//
//        }
//
//    }

//    public void connectToServer(String ip) {
//        Intent mainServiceConnect = new Intent(actionMainServiceConnect);
//        mainServiceConnect.putExtra("ip", ip);
//        sendBroadcast(mainServiceConnect);
//    }
//
//    public void updateData(JSONObject jsonObject) {
//        try {
//            String bat = jsonObject.getString("battery");
//            int charge = Integer.parseInt(bat.replaceAll("[^\\d.]", ""));
//            if (0 < charge && charge <= 20) {
//                if (bat.contains("Charg")){
//                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_20_black_24dp);
//                } else {
//                    imageView_battery.setImageResource(R.drawable.ic_battery_20_black_24dp);
//                }
//            } else if (20 < charge  && charge <= 30) {
//                if (bat.contains("Charg")){
//                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_30_black_24dp);
//                } else {
//                    imageView_battery.setImageResource(R.drawable.ic_battery_30_black_24dp);
//                }
//            } else if (30 < charge  && charge <= 50) {
//                if (bat.contains("Charg")){
//                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_50_black_24dp);
//                } else {
//                    imageView_battery.setImageResource(R.drawable.ic_battery_50_black_24dp);
//                }
//            } else if (50 < charge  && charge <= 60) {
//                if (bat.contains("Charg")){
//                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_60_black_24dp);
//                } else {
//                    imageView_battery.setImageResource(R.drawable.ic_battery_60_black_24dp);
//                }
//            } else if (60 < charge  && charge <= 80) {
//                if (bat.contains("Charg")){
//                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_80_black_24dp);
//                } else {
//                    imageView_battery.setImageResource(R.drawable.ic_battery_80_black_24dp);
//                }
//            } else if (80 < charge  && charge <= 90) {
//                if (bat.contains("Charg")){
//                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_90_black_24dp);
//                } else {
//                    imageView_battery.setImageResource(R.drawable.ic_battery_90_black_24dp);
//                }
//            } else if (90 < charge  && charge <= 100) {
//                if (bat.contains("Charg")){
//                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_full_black_24dp);
//                } else {
//                    imageView_battery.setImageResource(R.drawable.ic_battery_full_black_24dp);
//                }
//            }
//            batteryStatus.setText(charge + "%");
//            String net = jsonObject.getString("network");
//            String [] str = net.split(" ");
//            for (int i = 0; i < str.length; i ++) {
//                if (str[i].contains("wifi")) {
//                    String network = str[i+1];
//                }
//            }
//            networkStatus.setText("SSID: " + network);
//            String back = jsonObject.getString("backlight");
//            Double val = Double.valueOf(back);
//            int backlight = Integer.valueOf(val.intValue());
//            text_backlight.setText(String.valueOf(backlight));
//            seekBar_backlight.setProgress(backlight);
//            String snd = jsonObject.getString("sound").replaceAll("\\D+", "");
//            int soundVol = Integer.parseInt(snd);
//            seekBar_sound.setProgress(soundVol);
//
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "Json error: " + e);
//            e.printStackTrace();
//        }
//    }
//
//    public void changeButton(String parameter) {
//        if (parameter.contains("disable")) {
//            button_connect.setEnabled(false);
//        } else if (parameter.contains("close")) {
//            button_connect.setEnabled(true);
//            button_connect.setText("Close");
//        }
//    }

//    public class MainBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            context = context.getApplicationContext();
//            String action = intent.getAction();
//            String parameters = intent.getStringExtra("parameters");
//            if (action.contains("actionButton")) {
//                if (parameters.contains("Button_connect_disable")) {
//                    changeButton("disable");
//                } else if (parameters.contains("close")) {
//                    changeButton("close");
//                }
//            } else if (action.contains("UpdateData")) {
//                JSONObject jsonData = null;
//                try {
//                    jsonData = new JSONObject(intent.getStringExtra("parameters"));
//                } catch (JSONException e) {
//                    Log.e(LOG_TAG, "Json error: " + e);
//                    e.printStackTrace();
//                }
//
//                Log.d(LOG_TAG, "Broadcast in Main ACtivity");
//                updateData(jsonData);
//            } else if (action.contains("actionToFragmentListServers")) {
//                uiFindServers(intent);
//                Log.d(LOG_TAG, "Сервер найден");
//            }
//        }
//    }
//}