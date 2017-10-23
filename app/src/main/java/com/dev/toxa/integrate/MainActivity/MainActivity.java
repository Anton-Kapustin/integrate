package com.dev.toxa.integrate.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.dev.toxa.integrate.FragmentConnetctToServer.MVPfragmentConnectToServer;
import com.dev.toxa.integrate.FragmentConnetctToServer.PresenterFragmentConnectToServer;
import com.dev.toxa.integrate.FragmentListServers.MVPfragmentListServers;
import com.dev.toxa.integrate.FragmentListServers.PresenterFragmentListServers;
import com.dev.toxa.integrate.FragmentSettings.MVPFragmentSettings;
import com.dev.toxa.integrate.FragmentSettings.PresenterFragmentSettings;
import com.dev.toxa.integrate.LoggingNameClass;
import com.dev.toxa.integrate.R;
import com.dev.toxa.integrate.FragmentConnetctToServer.FragmentConnectToServer;
import com.dev.toxa.integrate.FragmentListServers.FragmentListServers;
import com.dev.toxa.integrate.FragmentSettings.FragmentSettings;
import com.dev.toxa.integrate.ActivitySharing.ActivitySharing;

public class MainActivity extends AppCompatActivity implements MVPmain.view, FragmentListServers.CallbackToActivity {

    private String LOG_TAG = (new LoggingNameClass().parseName(getClass().getName().toString())) + " ";

    private Toolbar toolbar;
    private TabLayout tabLayout;

    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    PresenterMain presenterMain;
    PresenterFragmentListServers presenterFragmentListServers;
    PresenterFragmentConnectToServer presenterFragmentConnectToServer;
    PresenterFragmentSettings presenterFragmentSettings;
    ActivitySharing activitySharing;

    Intent intentShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName().toString()));
        super.onCreate(savedInstanceState);

        presenterMain = new PresenterMain(this);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentListServers(), "Поиск серверов");
        adapter.addFragment(new FragmentConnectToServer(), "Управление ПК");
        adapter.addFragment(new FragmentSettings(), "Параметры");
        viewPager.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    //====================Создание презертеров фрагментов, прикрепление view к презентерам=========================
    @Override
    public PresenterFragmentListServers setFragmentListServers(MVPfragmentListServers.view view) {
        Log.i(LOG_TAG, "method name: " + Thread.currentThread().getStackTrace()[2].getMethodName());
        if (presenterFragmentListServers != null) {
            presenterFragmentListServers.setView(view);
            Log.d(LOG_TAG, "presenter list servers set view");
        } else {
            presenterFragmentListServers = new PresenterFragmentListServers(view);
            Log.d(LOG_TAG, "presenter list servers create");
        }
        return presenterFragmentListServers;
    }

    @Override
    public PresenterFragmentSettings setFragmentSettings(MVPFragmentSettings.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        MVPFragmentSettings.view fragmentSettings = (MVPFragmentSettings.view) getSupportFragmentManager().findFragmentByTag(viewPager.getId() + ":" + adapter.getItem(2));
        if (presenterFragmentSettings != null) {
            presenterFragmentSettings.setView(fragmentSettings);
        } else {
            presenterFragmentSettings = new PresenterFragmentSettings(fragmentSettings);
        }
        return presenterFragmentSettings;
    }

    @Override
    public PresenterFragmentConnectToServer setFragmentConnectToServer(MVPfragmentConnectToServer.view view) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (presenterFragmentConnectToServer != null) {
            presenterFragmentConnectToServer.setView(view);
        } else {
            presenterFragmentConnectToServer = new PresenterFragmentConnectToServer(view);
        }
        return presenterFragmentConnectToServer;
    }

    //=============================Отправка IP и servername во fragmentConnectToServer==========+++===============
    @Override
    public void connectToSetver(final String IP, final String serverName, final String distr) {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        Log.d(LOG_TAG, "IP для соединения с сервером: " + IP + " " + serverName);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenterFragmentConnectToServer.connectToServer(IP, serverName, distr);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        if (presenterFragmentConnectToServer != null) {
            String actionSend = Intent.ACTION_SEND;
            String actionMultiple = Intent.ACTION_SEND_MULTIPLE;
            intentShare = getIntent();
            String action = intentShare.getAction();
            String type = intentShare.getType();
            presenterMain.OnResume(actionSend, actionMultiple, action, type);
        } else {
            Log.d(LOG_TAG, "presenter connect null");
        }
    }

    @Override
    public void minimazeActivity() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        moveTaskToBack(true);
    }

    @Override
    public void getShareLink() {
        Log.i(LOG_TAG, "method name: " + String.valueOf(Thread.currentThread().getStackTrace()[2].getMethodName()));
        final String sharedText = intentShare.getStringExtra(Intent.EXTRA_TEXT);
        if (presenterFragmentConnectToServer != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    presenterFragmentConnectToServer.sendLink(sharedText);
                }
            });
        } else {
            Log.d(LOG_TAG, "fragment connect to server null");
        }
    }
    //=============================================================================================================
}
