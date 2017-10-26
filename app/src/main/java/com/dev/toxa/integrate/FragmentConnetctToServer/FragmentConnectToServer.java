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

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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

    RelativeLayout layout_root;

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
        presenter.fragmentDestroy();
        super.onDestroy();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
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
        layout_root = (RelativeLayout) rootView.findViewById(R.id.layout_root);
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

        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryState = context.registerReceiver(null, ifilter);

        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        return rootView;
    }

    @Override
    public String getBatteryState() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        final String[] state = {""};
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int chargeStatus = 0;
                BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
                int level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                chargeStatus = batteryState.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = chargeStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                        chargeStatus == BatteryManager.BATTERY_STATUS_FULL;
                state[0] = String.valueOf(level);
                if (isCharging) {
                    state[0] += " charging";
                }
                Log.d(LOG_TAG, "Заряд батареи телефона: " + level);
                Log.d(LOG_TAG, "Статус зарядки: " + isCharging);
            }
        });
        Log.d(LOG_TAG, "state[0]: " + state[0]);
        return state[0];
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

    @Override
    public void startNotifyService() {
        context.startService(intentServiceNotify);
    }

    @Override
    public void stopNotifyServie() {
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

    @Override
    public void bindNotifyService() {
        context.bindService(intentServiceNotify, serviceConnectionNotify, BIND_AUTO_CREATE);
    }

    @Override
    public void unbindNotifyService() {
        context.unbindService(serviceConnectionNotify);
    }

    @Override
    public void updateUiBattery(final String value, final String res) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                batteryStatus.setText(value);
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

}

