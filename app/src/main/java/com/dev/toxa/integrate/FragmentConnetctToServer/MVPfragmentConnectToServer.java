package com.dev.toxa.integrate.FragmentConnetctToServer;


import org.json.JSONObject;

public interface MVPfragmentConnectToServer {

    interface view {
        String getBatteryState();
        String getNetworkState();
        void startNotifyServie();
        void stopNotifyServie();
        void updateUiBattery(String value, String res);
        void updateUiNetwork(String network);
        void updateUiBacklight(int value);
        void updateUiSound(int value);
        void updateServerName(String serverName);
        void updateServerLogo(String res);
    }

    interface presenter {
        void setView (MVPfragmentConnectToServer.view view);
        void seekbarBacklightChanged(int value);
        void seekbarSoundChanged(int value);
        void sendLink(String data);
    }
}
