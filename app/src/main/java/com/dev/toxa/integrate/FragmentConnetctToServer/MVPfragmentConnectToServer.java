package com.dev.toxa.integrate.FragmentConnetctToServer;


public interface MVPfragmentConnectToServer {

    interface view {
        String getBatteryState();
        String getNetworkState();
        void startNotifyService();
        void stopNotifyService();
        void bindNotifyService();
        void unbindNotifyService();
        boolean getNotifyServiceState();
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
        void fragmentPause();
        void fragmentResume();
        void fragmentDestroy();
    }
}
