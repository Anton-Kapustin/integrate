package com.dev.toxa.integrate.FragmentListServers;

import android.content.Context;

public interface MVPfragmentListServers {

    interface view {
        Context getFragmentListContext();
        void displayFoundServer(String serverName, int ID, String IP, String macAddress, String distr);
        void bindSearchService();
        void unBindSearchService();
        void sendServerIDToActivity(int ID);
    }

    interface presenter {
        void fragmentLoaded();
        void setView(MVPfragmentListServers.view view);
//        void serverFound(String IP, String serverName, String macAddress, String distro);
        void clearData();
        void buttonServerClicked(String IPnDistr, String serverName);
        void onDestroy();
        void onPause();
        void onResume();
    }

}
