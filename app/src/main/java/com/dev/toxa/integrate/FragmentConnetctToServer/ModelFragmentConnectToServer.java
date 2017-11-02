package com.dev.toxa.integrate.FragmentConnetctToServer;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbHelper;

import java.util.Map;

public class ModelFragmentConnectToServer {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private String currentIP = null;
    private String currentServerName = null;
    private String currentMacAddress = null;
    private String currentDistr = null;
    private boolean isFavorite = false;
    private DbHelper dbHelper;

    public void setDbHelper(DbHelper dbHelper) {
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
        if (currentDistr.equals("Debian")) {
            currentDistr = "debian";
        } else {
            currentDistr = "ic_phonelink_off_black_24dp";
        }
        return currentDistr;
    }

    public boolean getIsFavorite() {
        Log.d(LOG_TAG, "isFavorite: " + isFavorite);
        return isFavorite;
    }
}
