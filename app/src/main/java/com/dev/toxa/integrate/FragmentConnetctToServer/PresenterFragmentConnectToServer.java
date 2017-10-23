package com.dev.toxa.integrate.FragmentConnetctToServer;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class PresenterFragmentConnectToServer implements MVPfragmentConnectToServer.presenter, ConnectToServer.CallbackToPresenter {

    //===================================Переменные=====================================================================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private  String sayHello = "Hello, add me to access list";

    private MVPfragmentConnectToServer.view view;

    private ConnectToServer connectToServer = new ConnectToServer(this);
    private ModelFragmentConnectToServer modelFragmentConnectToServer = new ModelFragmentConnectToServer();
    boolean statusConnection = false;
    Timer timer;
    //==================================================================================================================

    public PresenterFragmentConnectToServer (MVPfragmentConnectToServer.view view) {
        this.view = view;
    }

    @Override
    public void setView(MVPfragmentConnectToServer.view view) {
            this.view = view;
    }

    public void connectToServer(String IP, String serverName, String distr) {
        modelFragmentConnectToServer.setCurrentIP(IP);
        modelFragmentConnectToServer.setCurrentServerName(serverName);
        modelFragmentConnectToServer.setCurrentDistr(distr);
        connectToServer.sendMessage(IP, "info");
    }

    @Override
    public void seekbarBacklightChanged(int value) {
        connectToServer.sendMessage(modelFragmentConnectToServer.getCurrentIP(), "backlight////" + value);
    }

    @Override
    public void seekbarSoundChanged(int value) {
        connectToServer.sendMessage(modelFragmentConnectToServer.getCurrentIP(), "sound////" + value);

    }

    @Override
    public void sendLink(String data) {
        String message = "";
        if (data.startsWith("http://") || data.startsWith("ftp://") || data.startsWith("https://")) {
            message = "share_link////" + data;
        } else {
            message = "share_text////";
        }
        message += data;
        connectToServer.sendMessage(modelFragmentConnectToServer.getCurrentIP(), message);

    }

    @Override
    public void startServer() {
        if (!(connectToServer.getConnectionState())) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    sendPhoneInfo();
                }
            };
            timer.schedule(timerTask, 60000, 60000);
            connectToServer.runServer(modelFragmentConnectToServer.getCurrentIP());
        }
    }

    @Override
    public void stopServer() {
        connectToServer.setConnetionState(false);
    }

    @Override
    public void sendPhoneInfo() {
        if (view != null) {
            String message = "phone_info////" + "battery " + view.getBatteryState() + "&" + "network " +  view.getNetworkState();
            String IP = modelFragmentConnectToServer.getCurrentIP();
            connectToServer.sendMessage(IP, message);
        } else {
            Log.d(LOG_TAG, "null view");
        }

    }

    @Override
    public void updateData(JSONObject jsonData) {
        view.updateServerName(modelFragmentConnectToServer.getCurrentServerName());
        view.updateServerLogo(modelFragmentConnectToServer.getCurrentDistr());
        try {
            String bat = jsonData.getString("battery");
            int charge = Integer.parseInt(bat.replaceAll("[^\\d.]", ""));
            if (0 < charge && charge <= 20) {
                if (bat.contains("Charg")) {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_charging_20_black_24dp");
                } else {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_20_black_24dp");

                }
            } else if (20 < charge && charge <= 30) {
                if (bat.contains("Charg")) {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_charging_30_black_24dp");

                } else {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_30_black_24dp");

                }
            } else if (30 < charge && charge <= 50) {
                if (bat.contains("Charg")) {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_charging_50_black_24dp");

                } else {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_50_black_24dp");

                }
            } else if (50 < charge && charge <= 60) {
                if (bat.contains("Charg")) {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_charging_60_black_24dp");

                } else {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_60_black_24dp");

                }
            } else if (60 < charge && charge <= 80) {
                if (bat.contains("Charg")) {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_charging_80_black_24dp");

                } else {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_80_black_24dp");

                }
            } else if (80 < charge && charge <= 90) {
                if (bat.contains("Charg")) {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_charging_90_black_24dp");

                } else {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_90_black_24dp");

                }
            } else if (90 < charge && charge <= 100) {
                if (bat.contains("Charg")) {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_charging_full_black_24dp");

                } else {
                    view.updateUiBattery(String.valueOf(charge), "ic_battery_full_black_24dp");

                }
            }
            String network = null;
            String net = jsonData.getString("network");
            String[] str = net.split(" ");
            network = str[str.length - 1];
            network = "SSID: " + network;
            view.updateUiNetwork(network);
            String back = jsonData.getString("backlight");
            Double val = Double.valueOf(back);
            int backlight = Integer.valueOf(val.intValue());
            view.updateUiBacklight(backlight);
            String snd = jsonData.getString("sound").replaceAll("\\D+", "");
            int soundVol = Integer.parseInt(snd);
            view.updateUiSound(soundVol);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Json error: " + e);
            e.printStackTrace();
        }
    }
}
