package com.dev.toxa.integrate.FragmentSettings;

/**
 * Created by toxa on 16.10.17.
 */
public interface MVPFragmentSettings {

    interface view {

    }
    interface presenter {
        void setView(MVPFragmentSettings.view view);
    }
}
