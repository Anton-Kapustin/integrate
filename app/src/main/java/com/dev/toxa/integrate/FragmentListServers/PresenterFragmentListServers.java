package com.dev.toxa.integrate.FragmentListServers;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbHelper;


public class PresenterFragmentListServers implements MVPfragmentListServers.presenter, ServiceFindServers.Callback {

    //==========================================Переменные==============================================================
    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private MVPfragmentListServers.view view;
    private ModelFragmentListServers modelFragmentListServers = new ModelFragmentListServers();
    private DbHelper dbHelper;

    //==================================================================================================================

    public PresenterFragmentListServers(MVPfragmentListServers.view view) {
        this.view = view;
        dbHelper = new DbHelper(view.getFragmentListContext(), 1);
        modelFragmentListServers.setDbHelper(dbHelper);
    }

    @Override
    public void setView(MVPfragmentListServers.view view) {
        this.view = view;

        Log.d(LOG_TAG, "settings Presenter List Servers");
    }

    @Override
    public void clearData() {
        modelFragmentListServers.clearData();
    }

    @Override
    public void fragmentLoaded() {

    }

    @Override
    public void buttonServerClicked(String IPnDistr, String serverName) {
        Log.d(LOG_TAG, "Нажата клавиша: " + serverName);
        String[] tag = IPnDistr.split(",");
        Log.d(LOG_TAG, "IP: " + tag[0] + " mac: " + tag[1] + " Distr: " + tag[2]);
        int ID = modelFragmentListServers.addToDB(tag[0], serverName, tag[1], tag[2]);
        view.sendServerIDToActivity(ID);
    }

    @Override
    public void onDestroy() {
        view.unBindSearchService();
        modelFragmentListServers.clearData();

    }

    @Override
    public void onPause() {
        view.unBindSearchService();
//        modelFragmentListServers.clearData();
    }

    @Override
    public void onResume() {
        view.bindSearchService();
    }

    @Override
    public void foundServer(final String IP, final String serverName, final String macAddress, final String distro) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        int ID = modelFragmentListServers.addServer(IP, serverName, macAddress, distro);
        if (ID != 0) {
            view.displayFoundServer(serverName, ID, IP, macAddress, distro);
        } else {
            Log.d(LOG_TAG, "null ID");
        }
    }
}
