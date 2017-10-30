package com.dev.toxa.integrate.FragmentSettings;

import android.content.Context;

public interface MVPFragmentSettings {

    interface view {
        Context getFragmentContext();
        boolean checkNotificationPermission();
        void openSystemNotifySettings();
        void setCheckbox(boolean enabled);
    }

    interface presenter {
        void fragmentLoaded();
        void setView(MVPFragmentSettings.view view);
        void checkboxChanged(boolean enable);
    }
}
