package com.dev.toxa.integrate.FragmentSettings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.R;

public class FragmentSettings extends Fragment {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout_settings, container, false);
    }

}
