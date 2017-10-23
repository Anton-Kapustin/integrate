package com.dev.toxa.integrate.FragmentSettings;

import com.dev.toxa.integrate.LoggingNameClass;

/**
 * Created by toxa on 16.10.17.
 */
public class PresenterFragmentSettings implements MVPFragmentSettings.presenter {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";


    private MVPFragmentSettings.view view;

    public PresenterFragmentSettings(MVPFragmentSettings.view view) {
        this.view = view;
    }

    @Override
    public void setView(MVPFragmentSettings.view view) {
        this.view = view;
    }
}
