package com.dev.toxa.integrate.FragmentListServers;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;


public class PresenterFragmentListServers implements MVPfragmentListServers.presenter {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private MVPfragmentListServers.view view;
    private ModelFragmentListServers model = new ModelFragmentListServers();

    public PresenterFragmentListServers(MVPfragmentListServers.view view) {
        this.view = view;
        Log.d(LOG_TAG, "Creating Presenter List Servers");
    }

    @Override
    public void setView(MVPfragmentListServers.view view) {
        this.view = view;
        Log.d(LOG_TAG, "settings Presenter List Servers");
    }

    @Override
    public void serverFound(String IP, String serverName, String distro) {
        int ID = model.addServer(IP, serverName, distro);
        if (ID != 0) {
            view.displayFoundServer(IP, ID, serverName, distro);
        } else {
            Log.d(LOG_TAG, "null ID");
        }
    }

    @Override
    public void clearData() {
        model.clearData();
    }

    @Override
    public void fragmentLoaded() {
        if (view != null) {
            view.bindSearchService();
        } else {
            Log.d(LOG_TAG, "view null");
        }
    }

    @Override
    public void buttonServerClicked(String IPnDistr, String serverName) {
        Log.d(LOG_TAG, "Нажата клавиша: " + serverName);
        String[] tag = IPnDistr.split(",");
        Log.d(LOG_TAG, "IP: " + tag[0] + " Distr: " + tag[1]);
        view.sendIPtoConnectFragment(tag[0], serverName, tag[1]);
    }

    @Override
    public void onDestroy() {
        view.unBindSearchService();
        model.clearData();

    }

    @Override
    public void onPause() {
        view.unBindSearchService();
//        model.clearData();
    }

    @Override
    public void onResume() {
        view.bindSearchService();
    }
}
