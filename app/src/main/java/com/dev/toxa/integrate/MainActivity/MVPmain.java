package com.dev.toxa.integrate.MainActivity;

import android.content.Context;
import android.content.Intent;
import com.dev.toxa.integrate.FragmentConnetctToServer.MVPfragmentConnectToServer;
import com.dev.toxa.integrate.FragmentConnetctToServer.PresenterFragmentConnectToServer;
import com.dev.toxa.integrate.FragmentListServers.MVPfragmentListServers;
import com.dev.toxa.integrate.FragmentListServers.PresenterFragmentListServers;
import com.dev.toxa.integrate.FragmentSettings.MVPFragmentSettings;
import com.dev.toxa.integrate.FragmentSettings.PresenterFragmentSettings;

public interface MVPmain {
    interface view {
        Context getContext();
        PresenterFragmentConnectToServer setFragmentConnectToServer(MVPfragmentConnectToServer.view view);
        PresenterFragmentListServers setFragmentListServers(MVPfragmentListServers.view view);
        PresenterFragmentSettings setFragmentSettings(MVPFragmentSettings.view view);
        void passShareToPresenterConnect(String shared);
        boolean checkPermissions();
        void minimazeActivity();
    }

    interface presenter {

        void activityLoaded();
        void ActivityOnResume(String actionSend, String type, String sharedText);
        void ActivityOnPause();
        void ActivityOnDestroy();
    }
}
