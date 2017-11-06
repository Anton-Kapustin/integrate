package com.dev.toxa.integrate.ActivitySharing;

import android.content.Intent;

public interface MVPactivitySharing {

    interface view {
        String getSharedText();
        void handleSendImage();
        void handleSendMultipleImages();
        void finishActivity();
    }
    interface presenter {
        void activityCreated(String action, String type);
    }
}
