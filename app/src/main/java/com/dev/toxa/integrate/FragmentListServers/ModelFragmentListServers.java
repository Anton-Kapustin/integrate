package com.dev.toxa.integrate.FragmentListServers;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;

import java.util.ArrayList;
import java.util.List;

public class ModelFragmentListServers {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private List<String> serversIP = new ArrayList();
    private String serverName = null;
    private String distro = null;

    int addServer(String IP, String serverName, String distro) {
        if (serversIP != null) {
            if (serversIP.contains(IP)) {
                return 0;
            } else {
                serversIP.add(IP);
            }
        } else {
            Log.e(LOG_TAG, "Ошибка в массиве serversIP: Null");
        }
        this.serverName = serverName;
        this.distro = distro;
        return getID();
    }

    int getID() {
        return serversIP.size();
    }

    String getIP(int ID) {
        return serversIP.get(ID - 1);
    }

    public String getDistro() {
        return distro;
    }

    void clearData() {
        serversIP.clear();
        serverName = null;
        distro = null;
    }


}
