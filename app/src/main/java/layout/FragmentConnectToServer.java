package layout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.dev.toxa.integrate.R;
import org.json.JSONException;
import org.json.JSONObject;


public class FragmentConnectToServer extends Fragment {
    String LOG_TAG = "FragmentConnectToServer";
    String actionReceive = "com.dev.toxa.integrate.FragmentConnectToServer";
    String actionToCommnandService = "com.dev.toxa.integrate.CommnadService";
    String parameters = "parameters";

    ImageView imageView_battery;
    TextView batteryStatus;
    TextView networkStatus;
    TextView text_backlight;
    TextView text_sound;
    SeekBar seekBar_backlight;
    SeekBar seekBar_sound;

    Activity activity;

    MainBroadcastReceiver receiver = new MainBroadcastReceiver();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        activity.registerReceiver(receiver, new IntentFilter(actionReceive));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_connect_to_server, container, false);
        batteryStatus = (TextView) rootView.findViewById(R.id.batteryStatus);
        networkStatus = (TextView) rootView.findViewById(R.id.networkStatus);
        text_backlight = (TextView) rootView.findViewById(R.id.text_backlight);
        text_sound = (TextView) rootView.findViewById(R.id.text_sound);
        imageView_battery = (ImageView) rootView.findViewById(R.id.imageView_battery);
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
                int i = seekBar.getProgress();
                Log.d(LOG_TAG, "SeekBar backlight : " + i);
                Intent intentToCommandService = new Intent(actionToCommnandService);
                intentToCommandService.putExtra(parameters, "backlight");
                intentToCommandService.putExtra("data", i);
                activity.sendBroadcast(intentToCommandService);
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
                Log.d(LOG_TAG, "SeekBar sound : " + i);
                Intent intentToCommandService = new Intent(actionToCommnandService);
                intentToCommandService.putExtra(parameters, "sound");
                intentToCommandService.putExtra("data", i);
                activity.sendBroadcast(intentToCommandService);
            }
        });
        return rootView;
    }

    public void updateData(JSONObject jsonObject) {
        try {
            String bat = jsonObject.getString("battery");
            int charge = Integer.parseInt(bat.replaceAll("[^\\d.]", ""));
            if (0 < charge && charge <= 20) {
                if (bat.contains("Charg")) {
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_20_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_20_black_24dp);
                }
            } else if (20 < charge && charge <= 30) {
                if (bat.contains("Charg")) {
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_30_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_30_black_24dp);
                }
            } else if (30 < charge && charge <= 50) {
                if (bat.contains("Charg")) {
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_50_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_50_black_24dp);
                }
            } else if (50 < charge && charge <= 60) {
                if (bat.contains("Charg")) {
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_60_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_60_black_24dp);
                }
            } else if (60 < charge && charge <= 80) {
                if (bat.contains("Charg")) {
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_80_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_80_black_24dp);
                }
            } else if (80 < charge && charge <= 90) {
                if (bat.contains("Charg")) {
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_90_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_90_black_24dp);
                }
            } else if (90 < charge && charge <= 100) {
                if (bat.contains("Charg")) {
                    imageView_battery.setImageResource(R.drawable.ic_battery_charging_full_black_24dp);
                } else {
                    imageView_battery.setImageResource(R.drawable.ic_battery_full_black_24dp);
                }
            }
            String network = null;
            batteryStatus.setText(charge + "%");
            String net = jsonObject.getString("network");
            String[] str = net.split(" ");
            network = str[str.length - 1];
//            for (int i = 0; i < str.length; i++) {
//                if (str[i].contains("wifi")) {
//                    network = str[i + 1];
//                }
//            }
            network = "SSID: " + network;
            networkStatus.setText(network);
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

    public class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            context = context.getApplicationContext();
            try {
                String action = intent.getAction();
                String params = intent.getStringExtra("parameters");
                if (params.contains("updateData")) {
                    JSONObject jsonData = null;
                    try {
                        jsonData = new JSONObject(intent.getStringExtra("data"));
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Json error: " + e);
                        e.printStackTrace();
                    }
                    Log.d(LOG_TAG, "Broadcast in FragmentConnectToServer");
                    updateData(jsonData);
                }
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "null error: " + e);
            }
        }
    }
}

