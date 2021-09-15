package com.dev.toxa.integrate.FragmentConnetctToServer;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.Network.ClientConnect;
import com.dev.toxa.integrate.Network.ServerConnect;
import com.dev.toxa.integrate.db.DbHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class PresenterFragmentConnectToServer implements MVPfragmentConnectToServer.presenter,
         ServiceNotifyListener.CallbackToPressenter {

    //===================================Переменные=====================================================================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName())) + " ";

    private MVPfragmentConnectToServer.view view;

    private ModelFragmentConnectToServer modelFragmentConnectToServer = new ModelFragmentConnectToServer(this);
    private boolean statusConnection = false;
    private Timer timer;
    //==================================================================================================================

    public PresenterFragmentConnectToServer (MVPfragmentConnectToServer.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        this.view = view;
        DbHelper dbHelper = new DbHelper(view.getFragmentcontext(), 1);
        modelFragmentConnectToServer.setDbHelper(dbHelper);
    }

    @Override
    public void setView(MVPfragmentConnectToServer.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        this.view = view;
    }

    //============================================Нажития на кнопки/чекбоксы============================================

    @Override
    public void checkboxChecked(boolean enabled) {
        modelFragmentConnectToServer.setFavorite(enabled);
    }

    public void clickedOnFoundServer(int serverID) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        modelFragmentConnectToServer.getServerDataFromID(serverID);
        view.setCheckbox(modelFragmentConnectToServer.getIsFavorite());
        view.bindNotifyService();
        sendMessage("info");
    }

    @Override
    public void seekbarBacklightChanged(int value) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        sendMessage("backlight////" + value);
    }

    @Override
    public void seekbarSoundChanged(int value) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        sendMessage("sound////" + value);

    }

    //=============================================Жизненный цикл фрагмента=============================================

    @Override
    public void fragmentPause() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (view.getNotifyServiceState()) {
            view.unbindNotifyService();
        }
    }

    @Override
    public void fragmentResume() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (!(view.getNotifyServiceState())) {
            view.bindNotifyService();
        }
    }

    @Override
    public void fragmentDestroy() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (view.getNotifyServiceState()) {
            view.unbindNotifyService();
        }
    }

    //============================================Взаимодествие с сервером==============================================

    public void startServer() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "statusConnection: " + statusConnection);
        if (!(statusConnection)) {
            final ServerConnect server = new ServerConnect(this);
            statusConnection = true;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    server.runServer();
                }
            });
            thread.start();
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    sendPhoneInfo();
                }
            };
            timer.schedule(timerTask, 10000, 20000);
        }
    }

    public void stopServer() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        try {
            timer.cancel();
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, e.toString());
        }
        statusConnection = false;
        modelFragmentConnectToServer.setInUse(false);
        view.stopNotifyService();
    }

    private void sendMessage(final String message) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        final ClientConnect client = new ClientConnect(this);
        final String IP = modelFragmentConnectToServer.getCurrentIP();
        Log.d(LOG_TAG, "Даннык для отправки: " + message);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Адрес компа: " + message);
                client.sendToServer(IP, message);
            }
        });
        thread.start();
    }

    public void dataFromServerReceive(JSONObject receivedData) {
        try {
            if (receivedData.has("phone")) {
                threadToUI();
        }
            if (receivedData.has("PC_info")) {
                Log.d(LOG_TAG, "Получено: " + receivedData.toString());
                updateData(receivedData);
            }
        } catch (NullPointerException e) {
        Log.d(LOG_TAG, "null error: " + e);
        }
    }

    public boolean getStatusConnection() {
        return statusConnection;
    }

    //=============================================Получение данных о телефоне==========================================

    private void threadToUI(){
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        view.enterInUIthread();
    }

    @Override
    public void inUIthread() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        sendPhoneInfo();
    }

    private void sendPhoneInfo() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (view != null) {
            final String message = "phone_info////" + "battery " + view.getBatteryState() + "&" + "network " +  view.getNetworkState();
            Log.d(LOG_TAG, "message: " + message);
            sendMessage(message);
        } else {
            Log.d(LOG_TAG, "null view");
        }
    }
    //==================================================================================================================

     //Обновление данных о ПК
    private void updateData(JSONObject jsonData) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
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
            String network;
            String net = jsonData.getString("network");
            String[] str = net.split(" ");
            network = str[str.length - 1];
            network = "SSID: " + network;
            view.updateUiNetwork(network);
            String backlight = jsonData.getString("backlight");
            try {
                Double backlightData = Double.valueOf(backlight);
                int backlightValue = backlightData.intValue();
                view.updateUiBacklight(backlightValue);
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, e.toString());
            }
            String snd = jsonData.getString("sound").replaceAll("\\D+", "");
            try {
                int soundVol = Integer.parseInt(snd);
                view.updateUiSound(soundVol);
            } catch (NumberFormatException e) {
                Log.e(LOG_TAG, "Нет значения громкости");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Json error: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void sendSharedLink(String sharedText) {
        if (sharedText != null) {
            sendMessage(sharedText);
            Log.d(LOG_TAG, "отправлено");
        }
    }

    //================================================Уведомления из телефона===========================================
    @Override //Уведомление
    public void notifyPosted(String appName, String title, String text) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "Notify posted: " + appName + " " + title + " " + text);
        String message = "notify////name: " + appName + "/ " + "title: " + title + "/ " + "text: " + text;
        sendMessage(message);
    }

    @Override //Звонок
    public void calling(String appName, String title, String text) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "Calling: " + appName + " " + title + " " + text);
        String message = "notify////name: " + appName + "/ " + "title: " + title + "/ " + "text: " + text;
        sendMessage(message);
    }

    //==================================================================================================================
}
