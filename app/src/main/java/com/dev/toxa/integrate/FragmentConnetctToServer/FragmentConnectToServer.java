package com.dev.toxa.integrate.FragmentConnetctToServer;

import android.content.*;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.MainActivity.MainActivity;
import com.dev.toxa.integrate.R;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.BIND_AUTO_CREATE;


public class FragmentConnectToServer extends Fragment implements MVPfragmentConnectToServer.view {

    //==========================================Переменные==============================================================

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    Context context;

    ImageView imageView_battery;
    ImageView imageView_distr;

    CheckBox checkBoxFavorite;

    LinearLayout layout_root;

    TextView batteryStatus;
    TextView networkStatus;
    TextView text_backlight;
    TextView text_sound;
    TextView text_serverName;

    SeekBar seekBar_backlight;
    SeekBar seekBar_sound;

    MainActivity activity;
    PresenterFragmentConnectToServer presenter;

    IntentFilter ifilter;
    Intent batteryState;

    Intent intentServiceNotify;
    ServiceConnection serviceConnectionNotify;
    ServiceNotifyListener serviceNotifyListener;

    //==================================================================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity = (MainActivity) getActivity();
        presenter = activity.setFragmentConnectToServer(this);
        context = getContext();
        intentServiceNotify = new Intent(context, ServiceNotifyListener.class);
        serviceConnectionNotify = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.d(LOG_TAG, "Подключено к Notify Service");
                serviceNotifyListener = ((ServiceNotifyListener.NotifyServiceBinder) iBinder).getService();
                serviceNotifyListener.setPresenter(presenter);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(LOG_TAG, "Отключен от Notify Service");
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_connect_to_server, container, false);
        batteryStatus = (TextView) rootView.findViewById(R.id.batteryStatus);
        networkStatus = (TextView) rootView.findViewById(R.id.networkStatus);
        text_backlight = (TextView) rootView.findViewById(R.id.text_backlight);
        text_sound = (TextView) rootView.findViewById(R.id.text_sound);
        text_serverName = (TextView) rootView.findViewById(R.id.text_serverName);
        imageView_battery = (ImageView) rootView.findViewById(R.id.imageView_battery);
        imageView_distr = (ImageView) rootView.findViewById(R.id.imageView_distr);
        layout_root = (LinearLayout) rootView.findViewById(R.id.layout_root);
        checkBoxFavorite = (CheckBox) rootView.findViewById(R.id.checkBox_favorite);
        seekBar_backlight = (SeekBar) rootView.findViewById(R.id.seekBar_backlight);
        seekBar_sound = (SeekBar) rootView.findViewById(R.id.seekBar_sound);

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
                int value = seekBar.getProgress();
                Log.d(LOG_TAG, "SeekBar backlight : " + value);
                presenter.seekbarBacklightChanged(value);
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
                int value = seekBar.getProgress();
                Log.d(LOG_TAG, "SeekBar sound : " + value);
                presenter.seekbarSoundChanged(value);
            }
        });

        checkBoxFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.checkboxChecked(b);
            }
        });

        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryState = context.registerReceiver(null, ifilter);

        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        return rootView;
    }

    @Override
    public Context getFragmentcontext() {
        return getContext();
    }

    @Override
    public void setCheckbox(final boolean enabled) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkBoxFavorite.setChecked(enabled);
            }
        });
    }

    //===========================================Жизненный цикл=========================================================

    @Override
    public void onPause() {
        presenter.fragmentPause();
        super.onPause();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.fragmentResume();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));

        savedInstanceState.putString("batteryStatus", (String) batteryStatus.getText());
        savedInstanceState.putString("networkStatus", (String) networkStatus.getText());
        savedInstanceState.putString("text_backlight", (String) text_backlight.getText());
        savedInstanceState.putString("text_sound", (String) text_sound.getText());
        savedInstanceState.putString("text_serverName", (String) text_serverName.getText());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));

        if (savedInstanceState != null) {
            batteryStatus.setText(savedInstanceState.getString("batteryStatus"));
            networkStatus.setText(savedInstanceState.getString("networkStatus"));
            text_backlight.setText(savedInstanceState.getString("text_backlight"));
            text_sound.setText(savedInstanceState.getString("text_sound"));
            text_serverName.setText(savedInstanceState.getString("text_serverName"));
        }
    }

    //===========================================Показания датчиков=====================================================

    @Override
    public void enterInUIthread() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.inUIthread();
            }
        });
    }

    @Override
    public String getBatteryState() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        String state = "";
        int chargeStatus = 0;
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        chargeStatus = batteryState.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = chargeStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                chargeStatus == BatteryManager.BATTERY_STATUS_FULL;
        state = String.valueOf(level);
        if (isCharging) {
            state += " charging";
        }
        Log.d(LOG_TAG, "Заряд батареи телефона: " + level);
        Log.d(LOG_TAG, "Статус зарядки: " + isCharging);
        return state;
    }

    @Override
    public String getNetworkState() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();
        String networkStatus = "network " + carrierName;
        Log.d(LOG_TAG, "carrierName: " + carrierName);
        return networkStatus;
    }

    //===========================================Запуск сервиса=========================================================

    @Override
    public void bindNotifyService() {
        context.bindService(intentServiceNotify, serviceConnectionNotify, BIND_AUTO_CREATE);
    }

    @Override
    public void unbindNotifyService() {
        context.unbindService(serviceConnectionNotify);
    }

    @Override
    public void startNotifyService() {
        context.startService(intentServiceNotify);
    }

    @Override
    public void stopNotifyService() {
        context.stopService(intentServiceNotify);
    }

    @Override
    public boolean getNotifyServiceState() {
        if (serviceNotifyListener != null) {
            return true;
        } else {
            return false;
        }
    }

    //==========================================Обновление интерфейса===================================================

    @Override
    public void updateUiBattery(final String value, final String res) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                batteryStatus.setText(value + "%");
                imageView_battery.setImageResource(activity.getResources().getIdentifier(res,  "drawable", context.getPackageName()));
            }
        });

    }

    @Override
    public void updateUiNetwork(final String network) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkStatus.setText(network);
            }
        });
    }

    @Override
    public void updateUiBacklight(final int value) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar_backlight.setProgress(value);
            }
        });
    }

    @Override
    public void updateUiSound(final int value) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar_sound.setProgress(value);
            }
        });
    }

    @Override
    public void updateServerName(final String serverName) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text_serverName.setText(serverName);
            }
        });
    }

    @Override
    public void updateServerLogo(final String res) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView_distr.setImageResource(activity.getResources().getIdentifier(res, "drawable", context.getPackageName()));
            }
        });
    }

    //==================================================================================================================
}

