package com.dev.toxa.integrate.FragmentConnetctToServer;

import com.dev.toxa.integrate.LoggingNameClass;

public class ModelFragmentConnectToServer {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private String currentIP = null;
    private String currentServerName = null;
    private String currentDistr = null;

    public void setCurrentIP(String IP) {
        currentIP = IP;
    }

    public void setCurrentServerName(String serverName) {
        currentServerName = serverName;
    }

    public void setCurrentDistr(String distr) {
        if (distr.equals("Debian")) {
            currentDistr = "debian";
        } else {
            currentDistr = "ic_phonelink_off_black_24dp";
        }
    }

    public String getCurrentIP() {
        return currentIP;
    }

    public String getCurrentServerName() {
        return currentServerName;
    }

    public String getCurrentDistr() {
        return currentDistr;
    }
}
