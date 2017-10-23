package com.dev.toxa.integrate.FragmentListServers;

public interface MVPfragmentListServers {

    interface view {

        void displayFoundServer(String serverName, int ID, String IP, String distr);
        void bindSearchService();
        void unBindSearchService();
        void sendIPtoConnectFragment(String IP, String serverName, String distr);
    }

    interface presenter {
        void fragmentLoaded();
        void setView(MVPfragmentListServers.view view);
        void serverFound(String IP, String serverName, String distro);
        void clearData();
        void buttonServerClicked(String IPnDistr, String serverName);
        void onDestroy();
        void onPause();
        void onResume();
    }

}
