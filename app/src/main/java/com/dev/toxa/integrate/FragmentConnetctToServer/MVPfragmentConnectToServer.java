package com.dev.toxa.integrate.FragmentConnetctToServer;


import android.content.Context;

public interface MVPfragmentConnectToServer {

    interface view {
        Context getFragmentcontext();
        void setCheckbox(boolean enabled);
        String getBatteryState();
        String getNetworkState();
        void startNotifyService();
        void stopNotifyService();
        void bindNotifyService();
        void unbindNotifyService();
        boolean getNotifyServiceState();
        void enterInUIthread();
        void updateUiBattery(String value, String res);
        void updateUiNetwork(String network);
        void updateUiBacklight(int value);
        void updateUiSound(int value);
        void updateServerName(String serverName);
        void updateServerLogo(String res);
    }

    interface presenter {
        void setView (MVPfragmentConnectToServer.view view);
        void checkboxChecked(boolean enabled);
        void seekbarBacklightChanged(int value);
        void seekbarSoundChanged(int value);
        void inUIthread();
        void fragmentPause();
        void fragmentResume();
        void fragmentDestroy();
    }
}
