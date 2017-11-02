package com.dev.toxa.integrate.FragmentListServers;

import android.util.Log;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.db.DbHelper;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

public class ModelFragmentListServers {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private List<String> serversIP = new ArrayList();
    private String serverName = null;
    private String distro = null;
    private DbHelper dbHelper;

    public int addServer(String IP, String serverName, String macAddress, String distro) {
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

    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public int getID() {
        return serversIP.size();
    }

    public int addToDB(String IP, String serverName, String macAddress, String distro) {
        int ID = dbHelper.addToBaseServer(IP, serverName, macAddress, distro);
        Log.d(LOG_TAG, "ID: " + ID);
        return ID;
    }

    void clearData() {
        serversIP.clear();
        serverName = null;
        distro = null;
    }


}
