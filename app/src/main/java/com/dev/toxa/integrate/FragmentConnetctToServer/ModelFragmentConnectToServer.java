package com.dev.toxa.integrate.FragmentConnetctToServer;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbHelper;
import com.dev.toxa.integrate.ActivitySharing.ObservableShare;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ModelFragmentConnectToServer implements Observer{

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private String currentIP = null;
    private String currentServerName = null;
    private String currentMacAddress = null;
    private String currentDistr = null;
    private boolean isFavorite = false;
    private DbHelper dbHelper;
    private ObservableShare observableShare = ObservableShare.getInstance();
    private PresenterFragmentConnectToServer presenter;

    public ModelFragmentConnectToServer (PresenterFragmentConnectToServer presenter) {
        this.presenter = presenter;
    }

    public void setDbHelper(DbHelper dbHelper) {
        observableShare.addObserver(this);
        this.dbHelper = dbHelper;
    }

    public void getServerDataFromID(int serverID) {
        Map serverData = dbHelper.getServerData(serverID);
        if (!(serverData.get("IP").equals("0"))) {
            Log.d(LOG_TAG, "IP: " + serverData.get("IP"));
            currentIP = (String) serverData.get("IP");
            currentServerName = (String) serverData.get("serverName");
            currentDistr = (String) serverData.get("distr");
            currentMacAddress = (String) serverData.get("macAddress");
            int favorite = (int) serverData.get("favorite");
            Log.d(LOG_TAG, "favorite: " + favorite);
            if (favorite == 0) {
                isFavorite = false;
            } else if (favorite == 1) {
                isFavorite = true;
            }
        }
    }

    public void setFavorite(boolean favorite) {
        dbHelper.changeFavorite(favorite, currentIP, currentMacAddress);
    }

    public void setInUse(boolean inUse) {
        dbHelper.changeInUse(inUse, currentIP, currentMacAddress);
    }

    public String getCurrentIP() {
        return currentIP;
    }

    public String getCurrentServerName() {
        return currentServerName;
    }

    public String getCurrentDistr() {
        if (!currentDistr.matches("Debian|Ubuntu|Linux Mint")) {
            currentDistr = "ic_phonelink_off_black_24dp";
        }
        return currentDistr;
    }

    public boolean getIsFavorite() {
        Log.d(LOG_TAG, "isFavorite: " + isFavorite);
        return isFavorite;
    }

    @Override
    public void update(Observable observable, Object o) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "obj: " + o);
        presenter.sendSharedLink((String) o);
    }
}
