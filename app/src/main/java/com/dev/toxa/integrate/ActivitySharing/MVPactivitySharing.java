package com.dev.toxa.integrate.ActivitySharing;

public interface MVPactivitySharing {

    interface view {

    }
    interface presenter {
        void setSharingIntent(String actionSend, String actionMultiple, String actionReceive, String typeReceive);
        void sendLink(String link);
    }
}
