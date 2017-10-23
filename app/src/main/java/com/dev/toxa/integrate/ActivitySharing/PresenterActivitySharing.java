package com.dev.toxa.integrate.ActivitySharing;

import com.dev.toxa.integrate.Network.ClientConnect;

public class PresenterActivitySharing implements MVPactivitySharing.presenter {

//=================================Переменные==============================
    private ActivitySharing activitySharing;
//=========================================================================
    public PresenterActivitySharing (ActivitySharing activitySharing) {
        this.activitySharing = activitySharing;
    }


    @Override
    public void setSharingIntent(String actionSend, String actionMultiple, String actionReceive, String typeReceive) {

    }

    @Override
    public void sendLink(String link) {

    }
}
